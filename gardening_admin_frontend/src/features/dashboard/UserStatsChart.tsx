import { useEffect, useState } from "react";
import ReactECharts from "echarts-for-react";
import axiosInterceptor from "@/shared/api/axiosInterceptor.ts";

export const UserStatsChart = () => {
    const [chartData, setChartData] = useState<{ period: string; count: number }[]>([]);
    const [periodType, setPeriodType] = useState<"DAILY" | "WEEKLY" | "MONTHLY" | "YEARLY">("MONTHLY");
    const [startDate, setStartDate] = useState("2025-01-01");
    const [endDate, setEndDate] = useState("2025-12-31");

    // ✅ 데이터 가져오기 함수
    const fetchData = () => {
        axiosInterceptor
            .get("/stats/users", {
                params: {
                    periodType,
                    startDate,
                    endDate,
                },
            })
            .then((res) => {
                setChartData(res.data);
            })
            .catch((err) => {
                console.error("Error fetching user stats:", err);
            });
    };

    useEffect(() => {
        fetchData();
    }, [periodType, startDate, endDate]); // 날짜와 기간 타입 변경 시 다시 호출

    const option = {
        title: { text: `${periodType} 가입자 수` },
        tooltip: { trigger: "axis" },
        xAxis: {
            type: "category",
            data: chartData.map((d) => d.period),
        },
        yAxis: { type: "value" },
        series: [
            {
                name: "가입자 수",
                type: "line",
                data: chartData.map((d) => d.count),
            },
        ],
    };

    return (
        <div>
            {/* ✅ 기간 선택 버튼 */}
            <div className="flex gap-2 mb-4">
                <button
                    onClick={() => setPeriodType("DAILY")}
                    className={`px-3 py-1 rounded ${periodType === "DAILY" ? "bg-blue-500 text-white" : "bg-gray-200"}`}
                >
                    일별
                </button>
                <button
                    onClick={() => setPeriodType("WEEKLY")}
                    className={`px-3 py-1 rounded ${periodType === "WEEKLY" ? "bg-blue-500 text-white" : "bg-gray-200"}`}
                >
                    주별
                </button>
                <button
                    onClick={() => setPeriodType("MONTHLY")}
                    className={`px-3 py-1 rounded ${periodType === "MONTHLY" ? "bg-blue-500 text-white" : "bg-gray-200"}`}
                >
                    월별
                </button>
                <button
                    onClick={() => setPeriodType("YEARLY")}
                    className={`px-3 py-1 rounded ${periodType === "YEARLY" ? "bg-blue-500 text-white" : "bg-gray-200"}`}
                >
                    연별
                </button>
            </div>

            <div className="flex gap-4 mb-4">
                <div>
                    <label className="mr-2">시작 날짜:</label>
                    <input
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        className="border px-2 py-1 rounded"
                    />
                </div>
                <div>
                    <label className="mr-2">끝 날짜:</label>
                    <input
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        className="border px-2 py-1 rounded"
                    />
                </div>
            </div>

            {/* ✅ 차트 */}
            <ReactECharts option={option} style={{ height: 400 }} />
        </div>
    );
};