module.exports = {
    content: ["./src/**/*.{js,ts,jsx,tsx}"],
    theme: {},
    plugins: [
        require('tailwind-scrollbar')({ nocompatible: true })
    ],
};