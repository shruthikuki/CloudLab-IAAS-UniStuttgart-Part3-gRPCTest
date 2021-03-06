package cloudlab.ops;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cloudlab.TestOpsProto.Main;
import cloudlab.TestOpsProto.TestGrpc;
import io.grpc.stub.StreamObserver;

/*
 This class implements the test service interface generated by gRPC using the
 .proto file defined.
 */

public class TestOpsImpl implements TestGrpc.Test {

    private static final Logger logger = Logger.getLogger(TestOpsImpl.class.getName());

    @Override
    public void stringTest(Main.StringRequest request, StreamObserver<Main.StringReply> responseObserver) {
        Main.StringReply reply = Main.StringReply.newBuilder().setOutput("Hi " + request.getName() + "from " + request.getPlace())
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void integerTest(Main.IntegerRequest request, StreamObserver<Main.IntegerReply> responseObserver) {
        int sum = request.getFirstNumber() + request.getSecondNumber();

        Main.IntegerReply reply = Main.IntegerReply.newBuilder().setOutput("Sum is: " + sum).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void floatTest(Main.FloatRequest request, StreamObserver<Main.FloatReply> responseObserver) {
        float sum = request.getFirstNumber() + request.getSecondNumber();

        Main.FloatReply reply = Main.FloatReply.newBuilder().setOutput("Sum is: " + sum).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void doubleTest(Main.DoubleRequest request, StreamObserver<Main.DoubleReply> responseObserver) {
        double sum = request.getFirstNumber() + request.getSecondNumber();

        Main.DoubleReply reply = Main.DoubleReply.newBuilder().setOutput("Sum is: " + sum).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void boolTest(Main.EvenOddRequest request, StreamObserver<Main.EvenOddReply> responseObserver) {
        boolean bool;

        if (request.getNumber() % 2 == 0)
            bool = true;
        else
            bool = false;

        Main.EvenOddReply reply = Main.EvenOddReply.newBuilder().setOutput(bool).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void enumTest(Main.EnumRequest request, StreamObserver<Main.EnumReply> responseObserver) {
        Main.EnumReply reply = Main.EnumReply.newBuilder().setOutput("Status is: " + request.getStatus()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void repeatedTest(Main.Array request, StreamObserver<Main.StringReply> responseObserver) {
        Main.StringReply reply = Main.StringReply.newBuilder().setOutput("Items are: " + request.getItemsList()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void mapTest(Main.MapRequest request, StreamObserver<Main.StringReply> responseObserver) {
        responseObserver.onNext(Main.StringReply.newBuilder().setOutput(request.getMap().toString()).build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Main.StringRequest> clientStream(final StreamObserver<Main.StringReply> responseObserver) {
        return new StreamObserver<Main.StringRequest>() {
            Main.StringRequest completeRequest = Main.StringRequest.newBuilder().build();
            List<String> placesList = new ArrayList<String>();

            @Override
            public void onNext(Main.StringRequest value) {
                completeRequest = Main.StringRequest.newBuilder(value).mergeFrom(completeRequest).build();
                placesList.add(value.getPlace());
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.WARNING, "Error  in client Stream.", t);
            }

            @Override
            public void onCompleted() {
                StringBuilder places = new StringBuilder();
                for (String place : placesList) {
                    if (!place.equals(""))
                        places.append(place).append(",");
                }
                places.deleteCharAt(places.length() - 1);
                Main.StringReply reply = Main.StringReply.newBuilder().setOutput("Hi " + completeRequest.getName() + ". You're from " + places.toString()).build();
                responseObserver.onNext(reply);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void serverStream(Main.StringRequest request, StreamObserver<Main.StringStreamReply> responseObserver) {
        responseObserver.onNext(Main.StringStreamReply.newBuilder().setName(request.getName()).build());
        responseObserver.onNext(Main.StringStreamReply.newBuilder().setPlace(request.getPlace()).build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Main.StringRequest> bidirectionalStream(final StreamObserver<Main.StringStreamReply> responseObserver) {
        return new StreamObserver<Main.StringRequest>() {
            Main.StringRequest completeRequest = Main.StringRequest.newBuilder().build();
            List<String> placesList = new ArrayList<String>();

            @Override
            public void onNext(Main.StringRequest value) {
                completeRequest = Main.StringRequest.newBuilder(value).mergeFrom(completeRequest).build();
                placesList.add(value.getPlace());
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.WARNING, "Error  in bidirectional Stream.", t);
            }

            @Override
            public void onCompleted() {
                Main.StringStreamReply nameReply = Main.StringStreamReply.newBuilder().setName("Hi " + completeRequest.getName() + ". You are from the following places:").build();
                responseObserver.onNext(nameReply);

                for (String place : placesList) {
                    if (!place.equals(""))
                        responseObserver.onNext(Main.StringStreamReply.newBuilder().setPlace(place).build());
                }
                responseObserver.onCompleted();
            }
        };
    }
}
