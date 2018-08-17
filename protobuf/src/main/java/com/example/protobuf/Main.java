package com.example.protobuf;

import com.example.protobuf.protocol.InvitationProto;
import com.example.protobuf.protocol.InvitationProto.InviteResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import static com.example.protobuf.protocol.UserProto.User;
import static com.example.protobuf.protocol.VersionProto.Version;

public class Main {

    public static void main(String[] args) throws IOException {
        /*User user =
                User.newBuilder()
                        .setId(1)
                        .setName("test")
                        .setPhoneNumber("12345678910")
                        .setEmail("test@test.com")
                        .addDescription("nothing")
                        .addDescription("nothing1")
                        .setVersion(Version.newBuilder()
                                .setVersion(1))
                        .build();
        byte[] protoAsBytes = user.toByteArray();

        // deserialize
        User dUser = User.parseFrom(protoAsBytes);
        dUser.getDescriptionList().stream().forEach(System.out::println);
        System.out.println(dUser);

        long start = System.currentTimeMillis();
        HttpClient client = HttpClients.createDefault();
        HttpPut put = new HttpPut("http://106.75.81.82:8220/invitation/invite?token=88155bc5a9f2e1915160ef73055ae4d8");
        HttpEntity entity = new ByteArrayEntity(InvitationProto.InviteRequest.newBuilder()
            .setInvitationCode("E24t4").build().toByteArray());
        put.setEntity(entity);
        put.setHeader("content-type", "application/x-protobuf");

        HttpResponse response = client.execute(put);

        System.out.println("used time:" + (System.currentTimeMillis() - start));

        HttpEntity res = response.getEntity();
        System.out.println("length:" + res.getContentLength());
        byte[] body = new byte[(int) res.getContentLength()];
        response.getEntity().getContent().read(body);

        InviteResponse response1 = InviteResponse.parseFrom(body);
        System.out.println(response1);
        System.out.println(response1.getErrCode());
        System.out.println(response1.getErrMsg());*/

        String URL = "http://mobvoi-account/v2/account/info/wwid?wwid=%s&origin=vpa_cardstream";
        String wwids = "d1b1885376bbd26cf77a8631624a0a30,5b58643d22354b61abb2aa52a0e24438,b93675d8797b40c8a0e91befe0350ce1,5b54c505d3724e0d81ad1e0637e88d2c,8a58b3432e19124077fe4a0c18432dac,88cc40cf9d2d4d69b9d11221a3aaad12,bf1bb509db2615401e5e7b8e1d17acd2,eb64736e909442598148702eff142db6,7901b9318f69428aaf0be06cf66e6413,f322ec9bc9fb46ab9f8d00038bbbac81,2ca4e0a503f24590951929a2f9ed0f26,06074bdbc404495eb27007cf46be5452,847415851d6248dabc051247d1cb93e3,7bff701b5ec443369f5152127b010f83,33ebf174ee904891894304fc296fc4cd,fe4ac1b6952001cf08a210f377ce0608,41449a5c0c1545e6a978c0bf2a0b00f3,8097e3adb9a847ebae7ca89eee1ebb77,a56376ac65c2432aa3afca244f230030,e3c5263cad01494998a0f0cae0e50595,327b18098cc0479fa4f0a39c367fa9fc,876ef97bdaf642e79b12c46c5b682dbb,ec70dc74ba9543af824d8bb650aa5eba,52e94f56df5643b1976887d89c57f665,73b122bd77654d76926876505c6694a6,bbca40764d56426daaf8f837d649dd4c";


        for (String wwid : wwids.split(",")) {
          HttpClient client = HttpClients.createDefault();
          String url = String.format(URL, wwid);
          HttpGet get = new HttpGet(url);
          HttpResponse response = client.execute(get);
          byte[] bytes = new byte[(int)response.getEntity().getContentLength()];
          response.getEntity().getContent().read(bytes);
          String ret = new String(bytes);
          JSONObject json = new JSONObject(ret);
          String nickname = json.getJSONObject("base_info").optString("nickname", "");
          String phone = json.getJSONObject("base_info").optString("phone", "未知");
          System.out.println("nickname=" + nickname + ",wwid: = " + wwid + ", phone = " + phone);
        }

    }
}
