import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    port: 5174, // React app runs on this port
    proxy: {
      // Handles requests like /api/v1.0/register
      '/api': {
        target: 'http://localhost:8081', // Spring Boot backend
        changeOrigin: true,
        secure: false,
      },
      // Optional: If your frontend directly calls /api/v1.0/ endpoints
      '/api/v1.0': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
