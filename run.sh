#!/usr/bin/env bash
cd "$(dirname "$0")" || exit 1

if [ -f .env ]; then
    . .env
fi

export R2C=CLI

exec java -Xms20m -Xmx20m -jar build/libs/r2c.jar "$@"
