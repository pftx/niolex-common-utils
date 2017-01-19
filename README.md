niolex-common-utils
===================

`Common Utils` is a pure Java utility framework for faster Java development.

* commons-core Common utilities, including codec collection compress concurrent config file event net stream etc...
* commons-demo Useless, please skip.
* commons-framework Currenty only contain the SEDA framework.
* commons-notify The notify framework based on Zookeeper.
* commons-remote A remote server for developers to look inside the JVM and change the status etc...
* commons-seri Utilities for faster usage of Json Smile Kryo protocol_buffer protocol_stuff
* commons-spring Currently useless, only a email sender helper.
* commons-storage Under development.

Very Common, Very Base
======================
For now, commons-core(current 3.X), commons-seri(current 3.X), commons-remote(current 3.X), commons-framework(current 1.X) and commons-notify(current 1.X) are ready for use!!!
Other projects are still stabilizing...

# commons-core(current 3.X)

The commons-core project contains so many utilities, so we can not list them one by one. We will list some useful ones here:

### org.apache.niolex.commons.bean
* BeanUtil convert Java Bean from and to byte array, and merge the details of one Java Bean into another Java bean etc...
* MutableOne, One, Pair, Triple Java Bean container, mostly for store temporary references as function return value.

### org.apache.niolex.commons.codec
* Base16Util encode data from and to Base16(Hex) string.
* Base64Util encode data from and to Base64(MINE encoding, the default and URL safe format) string.
* IntegerUtil encode integer from and to 8, 4, 3, 2 bytes.
* KVBase64Util encode a pair of key value data into one Base64(URL safe format) string.
* MD5Util calculate MD5 signature and check MD5 signature.
* RSAUtil use the RSA algorithm to encode, decode, sign and verify data.
* StringUtil encode string to and from bytes as ASCII, UTF8, GBK; join and split string; other related utilities.

### org.apache.niolex.commons.coder
* AESCoder support encode and decode using the AES algorithm.
* Blowfish2Coder, DESCoder, RC2Coder, TripleDESCoder etc...

### org.apache.niolex.commons.collection
* Cache the in-memory cache implementation, currently we have ConcurrentLRUCache, LRUHashMap, MapAsCache, SegmentLFUCache.
* CircularList store values round-robin. After the list is full, we rewind the pointer to the fist element and replace it and so on.
* CollectionUtil utility methods to help operate collections, for example: concat, copy, intersection, isEmpty, isSingle ...
* CyclicIntArray This class encapsulate an int array, one can get the head and tail at any time. When there are more data than the capacity, we override the eldest element.

### org.apache.niolex.commons.compress
* Compressor compress and decompress object, byte array, string. Currently we have GZiper and ZLiber.
* JacksonUtil using Jackson library to serialize object to JSON and vice-versa.

### org.apache.niolex.commons.concurrent
* Blocker This is a waiting utility for clients to wait for the response and in the mean time hold the thread.
* BlockThread The start method will only return if the new thread is running.
* ConcurrentUtil Init ConcurrentMap and shutdownAndAwaitTermination for ExecutorService.
* InvokableExecutorService Wrap a standard ExecutorService and can invoke any method on any host object inside the thread pool.
* SimpleFuture A simple future implementation.
* Syncer The Syncer is a utility to decorate an object into synchronized object for concurrent use in multiple threads environment.
* ThreadUtil ThreadGroup related functions, sleep, join, wait related functions.

### org.apache.niolex.commons.config
* PropertiesWrapper wrap Java standard Properties, provides convenient methods for accessing trimmed string, int, boolean, long and double values.
* ReloadablePropertiesWrapper Monitor the changes of the properties file, and reload it immediately.

### org.apache.niolex.commons.control
* FrequencyCheck Controls the invoke frequency in batch.
* TimeControler This class control the invoke frequency by time and maximum invoke count.

### org.apache.niolex.commons.event
* ConcurrentEventDispatcher Dispatch events across multiple threads.

### org.apache.niolex.commons.file
* FileUtil Get(Set) file contents from(to) local disk.
* DirUtil Directory related operations.
* FileChannelUtil Read data from file channel and write into file channel.
* FileMonitor Monitoring the file changes from the file system and notify users.
* DirMonitor Monitoring the directory changes from the file system and notify users.

### org.apache.niolex.commons.hash
* ConsistentHash The implementation of ConsistentHash.
* DoubleHash The DoubleHash is to replace the ConsistentHash in some conditions.

### org.apache.niolex.commons.net
* DownloadUtil Download file from HTTP, FTP URL.
* NetUtil Internet related utilities.
* HTTPUtil This is a simple HTTP utility for GET and POST requests.
* HTTPClient This HTTP client class is used to help user control cookie and authentication. It is backed by HTTPUtil.
* RESTClient A convenient tool class to help you invoke the RESTful API.

### org.apache.niolex.commons.reflect
* FastFieldUtil, FastMethodUtil, FastNewInstance Using Reflect ASM to operate on Java bean to achieve high speed.
* FieldUtil, MethodUtil, ProxyUtil Using Java's Reflection API to operate on Java bean.
* TypeUtil Functions for type casting, type matching.

### org.apache.niolex.commons.stream
* LimitRateInputStream Restrict the input rate.
* StreamUtil Some common function for input and output streams.

### org.apache.niolex.commons.test
* OrderedRunner Run all the test cases marked by this runner in alphabetical order.
* AnnotationOrderedRunner Run all the test cases marked by this runner in the order specified by the annotation value.
* MappingGenerator Generate the mapping between DB column name and java field name.
* SQLGenerator Generate SQL by the corresponding Java entity class.
* Tester Help check the value equals and between the range.
* TidyUtil The utility to tidy string, generate formatted string table, etc.

### org.apache.niolex.commons.util
* DateTimeUtil Translate between string and Date Time.
* MathUtil calculate max, min, sum, avg, Standard Deviation etc.
* Runner Run a method in a newly created thread.
* SystemUtil System Environment and JVM related utility class.
* ThrowableUtil Get the first root cause, translate the throwable into string and vice-versa.
* CommonStatistics Calculate the commonly used statistics for a given data set and a given percentile.

# commons-seri(current 3.X)

The commons-seri project contains tools to serialize Java Beans.

### org.apache.niolex.commons.seri

### org.apache.niolex.commons.stream

License
==================================
This framework is under the Apache License, version 2.0
http://www.apache.org/licenses/LICENSE-2.0

