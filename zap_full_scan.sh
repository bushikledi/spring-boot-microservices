#!/bin/bash

# NOTE: Escape project directory and target
ESCAPED_CI_PROJECT_DIR=$(sed -e 's/[&\\/]/\\&/g; s/$/\\/' -e '$s/\\$//' <<<"$CI_PROJECT_DIR")
sed -i "s/CI_PROJECT_DIR/$ESCAPED_CI_PROJECT_DIR/g" zap/zap_config.conf

ESCAPED_TARGET=$(sed -e 's/[&\\/]/\\&/g; s/$/\\/' -e '$s/\\$//' <<<"$TARGET")
sed -i "s/TARGET/$ESCAPED_TARGET/g" zap/context.context

# Run ZAP full scan on order-service (8080)
/zap/zap-full-scan.py -d \
                      -t http://order-service:8080 \
                      -r $CI_PROJECT_DIR/$ZAP_REPORT-order.html \
                      -w $CI_PROJECT_DIR/$ZAP_ALERT_REPORT-order.md \
                      -n $CI_PROJECT_DIR/zap/context.context \
                      -U $USERNAME

# Run ZAP full scan on stock-service (8081)
/zap/zap-full-scan.py -d \
                      -t http://stock-service:8081 \
                      -r $CI_PROJECT_DIR/$ZAP_REPORT-stock.html \
                      -w $CI_PROJECT_DIR/$ZAP_ALERT_REPORT-stock.md \
                      -n $CI_PROJECT_DIR/zap/context.context \
                      -U $USERNAME

returnCode=0

# Check for vulnerabilities in the reports
if grep -q "Instances" $CI_PROJECT_DIR/$ZAP_ALERT_REPORT-order.md || grep -q "Instances" $CI_PROJECT_DIR/$ZAP_ALERT_REPORT-stock.md; then
  echo "DAST RESULT: There are vulnerabilities found. See the detailed report for more information."
  returnCode=1
fi

exit $returnCode
