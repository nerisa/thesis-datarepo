package com.nerisa.datarepo.service;

import com.nerisa.datarepo.firebase.Notification;
import com.nerisa.datarepo.incentive.CustodianLevel;
import com.nerisa.datarepo.model.User;
import com.nerisa.datarepo.model.Warning;
import org.json.simple.JSONObject;

import java.util.logging.Logger;

/**
 * Created by nerisa on 4/5/18.
 */
public class NotificationService {

    public static Logger LOG = Logger.getLogger(NotificationService.class.getSimpleName());

    public static void sendNewWarningNotification(Warning warning, Long monumentId, User custodian){
        Notification notification = new Notification("New warning", "A new warning has been posted for your monument", custodian.getToken());
        JSONObject warningObject = warning.prepareNotificationBody(monumentId);
        notification.sendNotification(warningObject);
    }

    public static void sendLevelUpgradeNotification(User custodian, CustodianLevel level){
        Notification notification = new Notification("Congratulations!!", "You have been upgraded a level", custodian.getToken());
        JSONObject incentiveObject = new JSONObject();
        incentiveObject.put("level", level.toString());
        incentiveObject.put("type", "incentive");
        notification.sendNotification(incentiveObject);
    }
}
