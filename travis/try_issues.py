import logging, os, requests, sys

logging.basicConfig(level=logging.DEBUG) #logging.WARNING
log = logging.getLogger(__name__)

repo = os.environ.get('TRAVIS_REPO_SLUG')
url = "https://github.com/%s/issues" % repo

try:
	log.debug("pinging github issues: {url}".format(url=url))
	r = requests.head(url)
	if r.status_code != 200:
		print(" ({code}) Issues not enabled!".format(code=r.status_code))
		print(" Please go to your github repository's settings and enable issue tracking,")
		print(" then restart the Travis build.")
		sys.exit(1)
except requests.ConnectionError:
	print("failed to connect")
	sys.exit(1)