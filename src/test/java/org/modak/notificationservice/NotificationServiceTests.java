
package org.modak.notificationservice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modak.notificationservice.dtos.NotificationRequest;
import org.modak.notificationservice.exceptions.ExceededRateException;
import org.modak.notificationservice.model.Notification;
import org.modak.notificationservice.model.NotificationRate;
import org.modak.notificationservice.model.NotificationType;
import org.modak.notificationservice.output.MessageService;
import org.modak.notificationservice.rates.NotificationRatesConfig;
import org.modak.notificationservice.repositories.NotificationsCache;
import org.modak.notificationservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTests {
    @InjectMocks
    NotificationService notificationService;
    @Mock
    NotificationRatesConfig notificationRatesConfig;
    @Mock
    NotificationsCache notificationsCache;
    @Mock
    MessageService messageService;
    NotificationRequest newsRequest;
    NotificationRequest marketingRequest;
    NotificationRequest statusRequest;

    private static final String RESULT_KEY = "result";
    private static final String NOTIFICATION_SENT_OK = "Notification sent OK";

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        doReturn(this.getNotificationRates()).when(notificationRatesConfig).getNotificationRates();

        newsRequest = buildRequest(NotificationType.NEWS);
        marketingRequest = buildRequest(NotificationType.MARKETING);
        statusRequest = buildRequest(NotificationType.STATUS);
    }

    @Test
    public void processNotification() {
        NotificationRequest request = statusRequest;

        doReturn(Collections.emptyList())
                .when(notificationsCache)
                .findAByUserIdAndType(eq(request.getUserId()), eq(request.getType()));

        ResponseEntity<?> response = notificationService.process(request);

        Mockito.verify(notificationsCache, times(1)).save(any());
        Mockito.verify(messageService, times(1)).send(anyString(), anyString(), anyString());

        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(((Map<String, String>)response.getBody()).get(RESULT_KEY), NOTIFICATION_SENT_OK);
    }

    @Test
    public void failToSendNotification() {
        NotificationRequest request = statusRequest;

        doThrow(new RuntimeException())
                .when(messageService)
                .send(eq(request.getType().getValue()), eq(request.getUserId()), eq(request.getBody()));

        doReturn(Collections.emptyList())
                .when(notificationsCache)
                .findAByUserIdAndType(eq(request.getUserId()), eq(request.getType()));

        RuntimeException runtimeException = Assert.assertThrows(RuntimeException.class, () -> notificationService.process(request));

        Mockito.verify(notificationsCache, times(0)).save(any());
    }

    @Test
    public void processStatusNotificationRateExceeded() {
        NotificationRequest request = statusRequest;

        doReturn(buildExceededNotificationsList(request.getType()))
                .when(notificationsCache)
                .findAByUserIdAndType(eq(request.getUserId()), eq(request.getType()));

        try {
            notificationService.process(request);
        } catch (ExceededRateException exception) {
            Assert.assertEquals(exception.getStatus(), HttpStatus.TOO_MANY_REQUESTS);
        }

        Mockito.verify(notificationsCache, times(0)).save(any());
        Mockito.verify(messageService, times(0)).send(anyString(), anyString(), anyString());
    }

    @Test
    public void processMarketingNotificationRateExceeded() {
        NotificationRequest request = marketingRequest;

        doReturn(buildExceededNotificationsList(request.getType()))
                .when(notificationsCache)
                .findAByUserIdAndType(eq(request.getUserId()), eq(request.getType()));

        try {
            notificationService.process(request);
        } catch (ExceededRateException exception) {
            Assert.assertEquals(exception.getStatus(), HttpStatus.TOO_MANY_REQUESTS);
        }

        Mockito.verify(notificationsCache, times(0)).save(any());
        Mockito.verify(messageService, times(0)).send(anyString(), anyString(), anyString());
    }

    @Test
    public void processNewsNotificationRateExceeded() {
        NotificationRequest request = newsRequest;

        doReturn(buildExceededNotificationsList(request.getType()))
                .when(notificationsCache)
                .findAByUserIdAndType(eq(request.getUserId()), eq(request.getType()));

        try {
            notificationService.process(request);
        } catch (ExceededRateException exception) {
            Assert.assertEquals(exception.getStatus(), HttpStatus.TOO_MANY_REQUESTS);
        }

        Mockito.verify(notificationsCache, times(0)).save(any());
        Mockito.verify(messageService, times(0)).send(anyString(), anyString(), anyString());
    }

    public Map<NotificationType, NotificationRate> getNotificationRates(){
        return Map.of(
                NotificationType.STATUS, new NotificationRate(2, Duration.ofMinutes(1)),
                NotificationType.MARKETING, new NotificationRate(1, Duration.ofDays(1)),
                NotificationType.NEWS, new NotificationRate(3, Duration.ofHours(3)));
    }

    public List<Notification> buildExceededNotificationsList(NotificationType type) {

        return getNotificationRates()
                .entrySet()
                .stream()
                .filter(notificationTypeNotificationRateEntry -> notificationTypeNotificationRateEntry.getKey().equals(type))
                .map(notificationTypeNotificationRateEntry -> notificationTypeNotificationRateEntry.getValue().getAllowedRate())
                .findFirst()
                .stream()
                .map(integer -> IntStream.range(0, (integer + 1)).mapToObj(i -> Notification.builder()
                        .uuid(UUID.randomUUID())
                        .userId("test-user")
                        .body("some message")
                        .timestamp(LocalDateTime.now().minus(outOfRangeMillisecondsByType(type)))
                        .build()).collect(Collectors.toList()))
                .flatMap(List::stream)
                .toList();
    }

    private Duration outOfRangeMillisecondsByType(NotificationType type) {
        switch (type) {
            case STATUS -> {
                return Duration.ofSeconds(50);
            }
            case NEWS -> {
                return Duration.ofHours(2);
            }
            case MARKETING -> {
                return Duration.ofHours(23);
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    private static NotificationRequest buildRequest(NotificationType type) {
        return NotificationRequest.builder()
                .type(type)
                .body("this is a notification")
                .userId("test-user")
                .build();
    }

}

