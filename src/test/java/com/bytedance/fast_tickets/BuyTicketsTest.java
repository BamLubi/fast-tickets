package com.bytedance.fast_tickets;

import com.baomidou.mybatisplus.extension.api.R;
import com.bytedance.fast_tickets.controller.InventoryController;
import com.bytedance.fast_tickets.controller.MainController;
import com.bytedance.fast_tickets.entity.Logs;
import com.bytedance.fast_tickets.utils.Result;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.UUID;

@SpringBootTest
public class BuyTicketsTest {
    @Autowired
    MainController mainController;

    @RepeatedTest(10)
    void buyTickets(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Logs logs = new Logs(uuid, "316b1727157949c0b296546ba8eeeb0f");
//        Result a = mainController.buyTicket(logs);
        WebAsyncTask<Result<String>> a = mainController.buyTicketAsync(logs);
        System.out.println(a.toString());
    }
}
