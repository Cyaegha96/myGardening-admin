package com.ggirick.gardening_admin_backend.dto.stats;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DailyQuotaResponse {
    private String day;
    private Quota quota;

    @Setter
    @Getter
    public static class Quota {
        private Identify identify;

        @Setter
        @Getter
        public static class Identify {
            private int count;
            private int total;
            private int remaining;

        }
    }
}