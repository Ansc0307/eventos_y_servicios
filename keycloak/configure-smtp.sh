#!/bin/sh
set -eu

KEYCLOAK_BASE_URL="${KEYCLOAK_BASE_URL:-http://keycloak:8080}"
KEYCLOAK_REALM="${KEYCLOAK_REALM:-eventos}"
KEYCLOAK_ADMIN="${KEYCLOAK_ADMIN:-admin}"
KEYCLOAK_ADMIN_PASSWORD="${KEYCLOAK_ADMIN_PASSWORD:-admin}"

if [ -z "${KEYCLOAK_SMTP_HOST:-}" ]; then
  echo "[keycloak-init] KEYCLOAK_SMTP_HOST not set; skipping SMTP config."
  exit 0
fi

KCADM="/opt/keycloak/bin/kcadm.sh"

# Wait until Keycloak is ready to accept admin logins
attempt=0
until "$KCADM" config credentials \
  --server "$KEYCLOAK_BASE_URL" \
  --realm master \
  --user "$KEYCLOAK_ADMIN" \
  --password "$KEYCLOAK_ADMIN_PASSWORD" \
  >/dev/null 2>&1
do
  attempt=$((attempt + 1))
  if [ "$attempt" -ge 30 ]; then
    echo "[keycloak-init] Failed to authenticate to Keycloak at $KEYCLOAK_BASE_URL"
    exit 1
  fi
  sleep 2
done

PORT="${KEYCLOAK_SMTP_PORT:-587}"
FROM="${KEYCLOAK_SMTP_FROM:-no-reply@eventos.local}"
FROM_NAME="${KEYCLOAK_SMTP_FROM_DISPLAY_NAME:-Eventos}"
STARTTLS="${KEYCLOAK_SMTP_STARTTLS:-true}"
SSL="${KEYCLOAK_SMTP_SSL:-false}"
AUTH="${KEYCLOAK_SMTP_AUTH:-true}"

"$KCADM" update "realms/$KEYCLOAK_REALM" \
  -s "smtpServer.host=$KEYCLOAK_SMTP_HOST" \
  -s "smtpServer.port=$PORT" \
  -s "smtpServer.from=$FROM" \
  -s "smtpServer.fromDisplayName=$FROM_NAME" \
  -s "smtpServer.starttls=$STARTTLS" \
  -s "smtpServer.ssl=$SSL" \
  -s "smtpServer.auth=$AUTH" \
  >/dev/null

if [ -n "${KEYCLOAK_SMTP_USER:-}" ]; then
  "$KCADM" update "realms/$KEYCLOAK_REALM" -s "smtpServer.user=$KEYCLOAK_SMTP_USER" >/dev/null
fi

if [ -n "${KEYCLOAK_SMTP_PASSWORD:-}" ]; then
  "$KCADM" update "realms/$KEYCLOAK_REALM" -s "smtpServer.password=$KEYCLOAK_SMTP_PASSWORD" >/dev/null
fi

echo "[keycloak-init] SMTP configured for realm '$KEYCLOAK_REALM' ($KEYCLOAK_SMTP_HOST:$PORT)."
