package com.sb.client.deadline;

import com.sb.client.BalanceStreamObserver;
import com.sb.client.MoneyStreamingResponse;
import com.sb.models.*;
import com.sb.server.deadline.DeadlineInterceptor;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeadlineClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup(){
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .intercept(new DeadlineInterceptor())
                .build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void balanceTest(){
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(7).build();

        try{
            Balance balance = this.blockingStub.getBalance(balanceCheckRequest);
            System.out.println(
                    "Received : "+balance.getAmount()
            );
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Test
    public void withdrawTest(){
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(6).setAmount(50).build();
        try {
            this.blockingStub.withDeadline(Deadline.after(2,TimeUnit.SECONDS)).withdraw(withdrawRequest).
                    forEachRemaining(money -> System.out.println("Received : "+ money.getValue()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void withdrawAsyncTest() throws InterruptedException{
        CountDownLatch latch = new CountDownLatch(1);
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(50).build();
        this.bankServiceStub.withdraw(withdrawRequest, new MoneyStreamingResponse(latch));
        latch.await();
        //Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
    }


}
