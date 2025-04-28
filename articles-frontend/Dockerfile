FROM theasp/clojurescript-nodejs:shadow-cljs-alpine as builder

WORKDIR /usr/src/articles_front/

COPY shadow-cljs.edn /usr/src/articles_front/
COPY package.json /usr/src/articles_front/
COPY package-lock.json /usr/src/articles_front/
COPY resources/ /usr/src/articles_front/resources
COPY src/ /usr/src/articles_front/src

COPY node_modules /usr/src/articles_front/node_modules

RUN npm run release

FROM nginx:1.17.5
RUN rm /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx

RUN cd /etc/nginx \
    && chmod +x /etc/nginx/nginx.conf

WORKDIR /var/www/articles_front
COPY --from=builder /usr/src/articles_front/resources/public/css/ /var/www/articles_front/css/
COPY --from=builder /usr/src/articles_front/resources/public/js/ /var/www/articles_front/js/
COPY --from=builder /usr/src/articles_front/resources/public/index.html /var/www/articles_front/
COPY --from=builder /usr/src/articles_front/node_modules/ /var/www/articles_front/node_modules/

# start nginx container
CMD /bin/bash -c "nginx -g 'daemon off;'"