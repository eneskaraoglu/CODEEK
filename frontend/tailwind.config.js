/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#f5f7ff',
          100: '#ebefff',
          200: '#d6deff',
          300: '#b8c5ff',
          400: '#96a3ff',
          500: '#646cff',
          600: '#535bf2',
          700: '#4147d5',
          800: '#3639a8',
          900: '#2e3085',
        },
      },
    },
  },
  plugins: [],
}
