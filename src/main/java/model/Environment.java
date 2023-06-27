package model;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Environment {

    private OS os;
    private VM vm;
    private JRE jre;


    @Data
    public class OS {
        private long virt_mem_committed;
        private String name;
        private int open_fd_count;
        private int available_processors;
        private long phys_mem_total;
        private String version;
        private String arch;
        private int max_fd_count;
        private long swap_space_free;
        private long phys_mem_free;
        private long swap_space_total;
    }

    @Data
    public class VM {
        private String termination;
        private String name;
        private Classloading classloading;
        private String mode;
        private List<String> args;
        private String vendor;
        private String spec_vendor;
        private String spec_name;
        private Compiler compiler;
        private OffsetDateTime start_iso;
        private List<Collector> collectors;
        private long start_unix_ms;
        private String version;
        private List<Pool> pools;
        private long uptime_ms;
        private String compressed_oops_mode;
        private Threads threads;
        private String spec_version;
        private Memory memory;

        @Data
        public static class Classloading {
            private int class_count;
            private int classes_total;
        }

        @Data
        public static class Compiler {
            private String name;
            private int compilation_time_ms;
        }

        @Data
        public static class Collector {
            private String name;
            private int collection_count;
            private int collection_time_ms;
        }

        @Data
        public static class Pool {
            private String name;
            private String type;
            private Usage usage;
            private Usage peak_usage;
        }

        @Data
        public static class Usage {
            private long init;
            private long used;
            private long committed;
            private long max;
        }

        @Data
        public static class Threads {
            private int thread_count;
            private int thread_count_max;
            private int daemon_thread_count;
            private int threads_total;
        }

        @Data
        public static class Memory {

            private HeapUsage heap_usage;
            private HeapUsage nonheap_usage;
            private int pending_finalization_count;

            @Data
            public static class HeapUsage {
                private long init;
                private long used;
                private long committed;
                private long max;
            }

        }

    }

    @Data
    public class JRE {
        private String java_version;
        private String name;
        private List<String> class_path;
        private String spec_version;
        private String spec_vendor;
        private String spec_name;
        private String home;
        private String version;
        private String java_vendor;
        private List<String> library_path;
    }
}
