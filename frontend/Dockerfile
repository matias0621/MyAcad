# Etapa 1: build de Angular
FROM node:20 AS build
WORKDIR /app

# Copiar package.json y package-lock.json
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar todo el código fuente
COPY . .

# Compilar Angular para producción
# Nota: '--' pasa argumentos a ng
RUN npm run build -- --configuration production --output-path=dist/frontend

# Etapa 2: imagen final con Nginx
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

# Copiar el build de Angular
COPY --from=build /app/dist/frontend . 

# Copiar nginx.conf si querés proxy a backend
# (si no necesitás proxy, comentar o eliminar)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto
EXPOSE 80

# Comando para iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]
