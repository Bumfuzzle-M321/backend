FROM ubuntu:latest
LABEL authors="jamie"

ENTRYPOINT ["top", "-b"]