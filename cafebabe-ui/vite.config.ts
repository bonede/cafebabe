import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: "../cafebabe-web/src/main/resources/public",
    emptyOutDir: true
  }
})
