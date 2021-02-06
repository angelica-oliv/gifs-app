change-file-permission:
	chmod +x scripts/ci/set-java-11.sh

install-java-11: change-file-permission
	scripts/ci/set-java-11.sh