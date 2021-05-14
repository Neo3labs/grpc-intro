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

## RPC - Types

 * Unary.
 * Server-streaming.
 * Client-streaming.
 * Bi-directional-streaming.

## gRPC - Error Codes / Status Response Codes

The following status codes can be returned in gRPC responses. 

| Code     | Status        | Notes                    |
| ---------| ------------- | ------------------------ |
|    0     |   __OK__	         | Return on Success        |
|    1     | __CANCELLED__	   | The operation was cancelled, typically by the caller.
|    2	  | __UNKNOWN__	      | For example, this error may be returned when a Status value received from another address space belongs to an error-space that isn't known in this address space. Also errors raised by APIs that don't return enough error information may be converted to this error.|
| 3	     | __INVALID_ARGUMENT__	| The client specified an invalid argument.|
| 4	     | __DEADLINE_EXCEEDED__ |	The deadline expired before the operation was complete. For operations that change the state of the system, this error may be returned even if the operation is completed successfully. For example, a successful response from a server which was delayed long enough for the deadline to expire.|
| 5	| __NOT_FOUND__	 | Some requested entity wasn't found.
| 6	| __ALREADY_EXISTS__	| The entity that a client attempted to create already exists.
| 7	| __PERMISSION_DENIED__	| The caller doesn't have permission to execute the specified operation. Don't use __PERMISSION_DENIED__ for rejections caused by exhausting some resource; use __RESOURCE_EXHAUSTED__ instead for those errors. Don't use __PERMISSION_DENIED__ if the caller can't be identified (use __UNAUTHENTICATED__ instead for those errors). To receive a __PERMISSION_DENIED__ error code doesn't imply the request is valid or the requested entity exists or satisfies other pre-conditions.|
| 8	| __RESOURCE_EXHAUSTED__	| Some resource has been exhausted, perhaps a per-user quota, or perhaps the entire file system is out of space.|
| 9	| __FAILED_PRECONDITION__ |	The operation was rejected because the system is not in a state required for the operation's execution. For example, the directory to be deleted is non-empty or an rmdir operation is applied to a non-directory.|
| 10	| __ABORTED__	| The operation was aborted, typically due to a concurrency issue such as a sequencer check failure or transaction abort.|
| 11	| __OUT_OF_RANGE__	| The operation was attempted past the valid range.|
| 12	| __UNIMPLEMENTED__ |	The operation isn't implemented or isn't supported/enabled in this service.|
| 13	| __INTERNAL__ |	Internal errors. This means that some invariants expected by the underlying system have been broken. This error code is reserved for serious errors.|
| 14	| __UNAVAILABLE__ |	The service is currently unavailable. This is most likely a transient condition that can be corrected if retried with a backoff.|
| 15	| __DATA_LOSS__	| Unrecoverable data loss or corruption.|
| 16	| __UNAUTHENTICATED__ |	The request doesn't have valid authentication credentials for the operation.|

Sometimes multiple error codes may apply. Services should return the most specific error code that applies. For example, prefer __OUT_OF_RANGE__ over __FAILED_PRECONDITION__ if both codes apply. Similarly, prefer __NOT_FOUND__ or __ALREADY_EXISTS__ over __FAILED_PRECONDITION__.

### FAILED_PRECONDITION vs. ABORTED vs. UNAVAILABLE

The following is a litmus test that may help you decide between __FAILED_PRECONDITION__, __ABORTED__, and __UNAVAILABLE__:

 * Use __UNAVAILABLE__ if the client can retry just the failing call.
 * Use __ABORTED__ if the client should retry at a higher-level, such as when a client-specified test-and-set fails which indicates that the client should restart a read-modify-write sequence.
 * Use __FAILED_PRECONDITION__ if the client should not retry until the system state has been explicitly fixed. For example, if an "rmdir" fails because the directory is non-empty, it is best to return __FAILED_PRECONDITION__ because the client should not retry unless the files are deleted from the directory.

### INVALID_ARGUMENT vs. FAILED_PRECONDITION vs. OUT_OF_RANGE

The following is a litmus test that may help you decide between __INVALID_ARGUMENT__, __FAILED_PRECONDITION__, and __OUT_OF_RANGE__:

 * Use __INVALID_ARGUMENT__ if the arguments are problematic regardless of the state of the system. For example: a malformed URL.
 * Use __OUT_OF_RANGE__ if a value is out of range due to the state of the system. For example, the start_date is before start_date_restrict.
 * Use __FAILED_PRECONDITION__ if the value is invalid due to the state of the system, but isn't an __OUT_OF_RANGE__ value.

