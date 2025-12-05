import path from "path"
import tailwindcss from "@tailwindcss/vite"
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import svgr from 'vite-plugin-svgr'

// https://vite.dev/config/
export default defineConfig({
    plugins: [react(), tailwindcss(), svgr()],
    resolve: {
        alias: {
            "@": path.resolve(__dirname, "./src"),
        },
    },
    server: {
        host: '0.0.0.0', // 모든 네트워크 인터페이스에서 접근 가능
        port: 5174,       // 기본 포트 (원하면 변경 가능)
    },
})
