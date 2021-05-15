package com.sb.server;

import com.sb.models.Account;
import com.sb.models.TransferRequest;
import com.sb.models.TransferResponse;
import com.sb.models.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> transferResponseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int fromAccount = transferRequest.getFromAccount();
        int toAccount = transferRequest.getToAccount();
        int amount = transferRequest.getAmount();
        int balance = BankDB.getBalance(fromAccount);
        TransferStatus status = TransferStatus.FAILED;

        if(balance >= amount && fromAccount != toAccount){
            BankDB.deductBalance(fromAccount, amount);
            BankDB.addBalance(toAccount, amount);
            status = TransferStatus.SUCCESS;
        }
        Account fromAccountInfo = Account.newBuilder().setAccountNumber(fromAccount).setAmount(BankDB.getBalance(fromAccount)).build();
        Account toAccountInfo = Account.newBuilder().setAccountNumber(toAccount).setAmount(BankDB.getBalance(fromAccount)).build();
        TransferResponse transferResponse = TransferResponse.newBuilder().setStatus(status).addAccounts(fromAccountInfo).addAccounts(toAccountInfo).build();

        this.transferResponseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        BankDB.printAccountDetails();
        this.transferResponseStreamObserver.onCompleted();
    }
}
