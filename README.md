## gRPC - Introduction

 * High-performance, open-source RPC framework.
 * Developed at Google.
 * Client app directly invokes Server method on a different machine.
 * Serivce is defined using proto.

## gRPC - Synchronous vs Asynchronous

 * Client's call to the server can be Sync/Async
    *  Sync → blocking/waiting for the response.
    *  Async → Register a listner for callback
 * It is completely up to the RPC
    * It also depends on the RPC
