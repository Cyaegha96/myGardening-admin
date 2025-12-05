import type { AuthProvider } from "ra-core";
import axiosInterceptor from "@/shared/api/axiosInterceptor";

export const authProvider: AuthProvider = {
    async login({ id, password }) {
        const res = await axiosInterceptor.post("/auth/login", { id, password });
        const { accessToken, refreshToken } = res.data;

        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);

        return Promise.resolve();
    },

    async logout() {
        const accessToken = localStorage.getItem("accessToken");
        const refreshToken = localStorage.getItem("refreshToken");

        if (accessToken && refreshToken) {
            try {
                await axiosInterceptor.post("/auth/logout", { accessToken, refreshToken });
            } catch (err) {
                console.error("서버 로그아웃 실패", err);
            }
        }

        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");

        return Promise.resolve();
    },

    async getIdentity() {
        const res = await axiosInterceptor.get("/auth/me");

        const { id, nickname, avatarUrl } = res.data;
        return {
            id,
            fullName: nickname,   // react-admin은 fullName을 기본적으로 표시
            avatar: avatarUrl,    // UserMenu에서 avatar 사용
        };

    },


    checkError: () => Promise.resolve(),

    async checkAuth() {
        const token = localStorage.getItem("accessToken");
        if (!token) {
            return Promise.reject();
        }
        // 예: 토큰 만료 확인
        const payload = JSON.parse(atob(token.split(".")[1]));
        if (payload.exp * 1000 < Date.now()) {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            return Promise.reject();
        }
        return Promise.resolve();
    },

    async getPermissions() {
        return Promise.resolve();
    },
};