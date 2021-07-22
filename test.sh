URL="https://wl6sv63rcd.execute-api.us-east-2.amazonaws.com/dev/calc"


curl -XPOST $URL\
	 -H "Content-Type: application/json"\
	 --data '{"expr": "5 + 5"}'
