FROM golang AS builder
ENV CGO_ENABLED=0
WORKDIR /go/src/app/vendor/github.com/FINTLabs/fint-core-information-model
ARG VERSION=0.0.0
COPY . .
RUN go build -v -ldflags "-X main.Version=${VERSION}" -o /go/bin/fint-model .
RUN /go/bin/fint-model --version

FROM gcr.io/distroless/static
VOLUME [ "/src" ]
WORKDIR /src
COPY --from=builder /go/bin/fint-model /usr/bin/fint-model
ENTRYPOINT [ "/usr/bin/fint-model" ]
