FROM node:26-alpine AS builder

WORKDIR /app

# Install dependencies first so Docker layer caching works well.
COPY package*.json ./
RUN npm ci

# Copy source files and build the app.
COPY . .
RUN npm run build -- --configuration production

FROM nginx:stable-alpine

RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/dist/modsync-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
