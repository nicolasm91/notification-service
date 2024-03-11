package org.modak.notificationservice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modak.notificationservice.controllers.NotificationController;
import org.modak.notificationservice.dtos.NotificationRequest;
import org.modak.notificationservice.exceptions.ExceededRateException;
import org.modak.notificationservice.model.NotificationType;
import org.modak.notificationservice.service.NotificationService;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTests {
    @InjectMocks
    NotificationController notificationController;
    @Mock
    NotificationService notificationService;

    @Before
    public void init(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldThrowExceededRateException() {
        NotificationRequest request = buildRequest();
        doThrow(new ExceededRateException(""))
                .when(notificationService).process(eq(request));

        ExceededRateException exceededRateException = Assert.assertThrows(ExceededRateException.class, () -> notificationController.handleNotification(request));

        Assert.assertEquals(HttpStatus.TOO_MANY_REQUESTS, exceededRateException.getStatus());
    }

    private static NotificationRequest buildRequest() {
        return NotificationRequest.builder()
                .type(NotificationType.NEWS)
                .body("this is a notification")
                .userId("test-user")
                .build();
    }
}
