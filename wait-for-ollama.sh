#!/bin/sh
# wait-for-ollama.sh

set -e

host="$1"
shift
cmd="$@"

echo "⏳ Waiting for Ollama at $host..."

until curl -s "$host/api/tags" >/dev/null 2>&1; do
  >&2 echo "Ollama is unavailable - retrying..."
  sleep 3
done

>&2 echo "✅ Ollama is up - starting Spring Boot app"
exec $cmd
