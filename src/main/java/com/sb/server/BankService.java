package com.sb.server;

import com.sb.models.Balance;
import com.sb.models.BalanceCheckRequest;
import com.sb.models.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder().setAmount(accountNumber * 10).build();

        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }
}
