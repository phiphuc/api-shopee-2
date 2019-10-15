package com.shopee.tool.service;

import com.google.gson.Gson;
import domain.shopee.request.*;
import domain.shopee.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopeeService {

    private final Logger log = LoggerFactory.getLogger(ShopeeService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${cloud.topic.username.response}")
    private String usernameResponse;

    @KafkaListener(topics = "${cloud.topic.login.request}", groupId = "shopee")
    @Async
    public void login(String msg,
                      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                      @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        LoginResponse response = new LoginResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("Login request: " + msg);
            Gson requestGson = new Gson();
            LoginRequest loginRequest = requestGson.fromJson(msgTemp, LoginRequest.class);
            Login login = new Login();
            response = login.getLogin(loginRequest);

        } catch (Exception e) {

        }
        this.kafkaTemplate.send("${cloud.topic.login.response}", response.toString());
    }

    @KafkaListener(topics = "${cloud.topic.otp.request}", groupId = "shopee")
    @Async
    public void getOtp(String msg,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                       @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        OtpResponse otpResponse = new OtpResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("Get otp request: " + msg);
            Gson requestGson = new Gson();
            OtpRequest otpRequest = requestGson.fromJson(msgTemp, OtpRequest.class);
            Otp otp = new Otp();
            otpResponse = otp.getOtp(otpRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Get otp response: " + otpResponse.toString());
        this.kafkaTemplate.send("${cloud.topic.otp.response}", otpResponse.toString());
    }

    @KafkaListener(topics = "${cloud.topic.follow.request}", groupId = "shopee")
    @Async
    public void follow(String msg,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                       @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        FollowResponse followResponse = new FollowResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("Follow request: " + msg);
            Gson requestGson = new Gson();
            FollowRequest followRequest = requestGson.fromJson(msgTemp, FollowRequest.class);
            Follow follow = new Follow();
            followResponse =  follow.follow(followRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Follow response: " + followResponse.toString());
        this.kafkaTemplate.send("${cloud.topic.follow.response}", followResponse.toString());
    }


    @KafkaListener(topics = "${cloud.topic.username.request}", groupId = "shopee")
    @SendTo()
    public String getIdsByUsername(String msg,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                       @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        GetIdByUsernameKafkaResponse response = new GetIdByUsernameKafkaResponse();
        try {
            String msgTemp = String.valueOf(msg);
            Gson requestGson = new Gson();
            GetInformationRequest request = requestGson.fromJson(msgTemp, GetInformationRequest.class);
            log.info("Get ip by username request: " + request);
            Ids ids = new Ids();
            GetIdsByUsernameShopeeResponse getIdsByUsernameShopeeResponse =  ids.getIdsByUsername(request.getUsername());
            response.setId(request.getId());
            if(!getIdsByUsernameShopeeResponse.getError().equals("0")){
                response.setError(getIdsByUsernameShopeeResponse.getError() == null ? "": getIdsByUsernameShopeeResponse.getError());
                response.setError_msg(getIdsByUsernameShopeeResponse.getError_msg() == null ? "" : getIdsByUsernameShopeeResponse.getError_msg() );
                response.setVersion("");
                response.setAddress("");
                response.setFollow("");
                response.setFollowing("");
                response.setRate("");
                response.setProduct("");
                response.setShopId("");
                response.setName("");
            }else{
                response.setError(getIdsByUsernameShopeeResponse.getError() == null ? "": getIdsByUsernameShopeeResponse.getError());
                response.setError_msg(getIdsByUsernameShopeeResponse.getError_msg() == null ? "" : getIdsByUsernameShopeeResponse.getError_msg() );
                response.setVersion(getIdsByUsernameShopeeResponse.getVersion() == null ? "" : getIdsByUsernameShopeeResponse.getVersion());
                response.setAddress(getIdsByUsernameShopeeResponse.getData().getShop_location() ==  null ? "" : getIdsByUsernameShopeeResponse.getData().getShop_location());
                response.setFollow(getIdsByUsernameShopeeResponse.getData().getAccount().getFollowing_count() == null ? "" :getIdsByUsernameShopeeResponse.getData().getAccount().getFollowing_count());
                response.setFollowing(getIdsByUsernameShopeeResponse.getData().getFollower_count() == null ? "" :getIdsByUsernameShopeeResponse.getData().getFollower_count().toString());
                response.setRate(getIdsByUsernameShopeeResponse.getData().getRating_star() == null ? "" : getIdsByUsernameShopeeResponse.getData().getRating_star().toString());
                response.setProduct(getIdsByUsernameShopeeResponse.getData().getItem_count() == null ? "" : getIdsByUsernameShopeeResponse.getData().getItem_count().toString());
                response.setShopId(getIdsByUsernameShopeeResponse.getData().getShopid() == null ? "" : getIdsByUsernameShopeeResponse.getData().getShopid().toString() );
                response.setName(getIdsByUsernameShopeeResponse.getData().getName() == null ? "" : getIdsByUsernameShopeeResponse.getData().getName());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Get ip by username response: " + response.toString());
        return response.toString();
    }


    @KafkaListener(topics = "${cloud.topic.information-shop.request}", groupId = "shopee")
    @Async
    public void getInformationShop(String msg,
                                 @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                                 @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                                 @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        InformationShopeeResponse response = new InformationShopeeResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("Information shop request: " + msg);
            InformationShop informationShop = new InformationShop();
            response =  informationShop.getInformationWithCookie(msgTemp);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Information shop response: " + response.toString());
        this.kafkaTemplate.send("${cloud.topic.information-shop.response}", response.toString());
    }

    @KafkaListener(topics = "${cloud.topic.items-shop.request}", groupId = "shopee")
    @Async
    public void getItemShop(String msg,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                                   @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        GetItemShopeeResponse response = new GetItemShopeeResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("Get item shop request: " + msg);
            Gson requestGson = new Gson();
            ItemShopRequest itemShopRequest = requestGson.fromJson(msgTemp, ItemShopRequest.class);
            ItemsShop itemsShop = new ItemsShop();
            response =  itemsShop.getItemsShop(itemShopRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Get item shop response: " + response.toString());
        this.kafkaTemplate.send("${cloud.topic.login.response}", response.toString());
    }

    @KafkaListener(topics = "${cloud.topic.like-item.request}", groupId = "shopee")
    @Async
    public void LikeItem(String msg,
                            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                            @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LikeResponse response = new LikeResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("Like item shop request: " + msg);
            Gson requestGson = new Gson();
            LikeRequest request = requestGson.fromJson(msgTemp, LikeRequest.class);
            Like likeItem = new Like();
            response =  likeItem.like(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Like item shop response: " + response.toString());
        this.kafkaTemplate.send("like-item-response", response.toString());
    }

    @KafkaListener(topics = "${cloud.topic.unfollow.request}", groupId = "shopee")
    @Async
    public void unfollow (String msg,
                         @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                         @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        UnfollowResponse response = new UnfollowResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("Unfollow shop request: " + msg);
            Gson requestGson = new Gson();
            UnFollowRequest request = requestGson.fromJson(msgTemp, UnFollowRequest.class);
            Unfollow unfollow = new Unfollow();
            response =  unfollow.unfollow(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Unfollow shop response: " + response.toString());
        this.kafkaTemplate.send("unfollow-response", response.toString());
    }

    @KafkaListener(topics = "${cloud.topic.unlike.request}", groupId = "shopee")
    @Async
    public void unlike (String msg,
                          @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                          @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        UnlikeResponse response = new UnlikeResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("Unlike shop request: " + msg);
            Gson requestGson = new Gson();
            UnlikeRequest request = requestGson.fromJson(msgTemp, UnlikeRequest.class);
            Unlike unlike = new Unlike();
            response =  unlike.unlike(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Unlike shop response: " + response.toString());
        this.kafkaTemplate.send("unlike-response", response.toString());
    }

    @KafkaListener(topics = "${cloud.topic.view.request}", groupId = "shopee")
    @Async
    public void view (String msg,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        ViewResponse response = new ViewResponse();
        try {
            String msgTemp = String.valueOf(msg);
            log.info("View item request: " + msg);
            Gson requestGson = new Gson();
            ViewRequest request = requestGson.fromJson(msgTemp, ViewRequest.class);
            View view = new View();
            response =  view.view(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("View item response: " + response.toString());
        this.kafkaTemplate.send("view-response", response.toString());
    }

}
