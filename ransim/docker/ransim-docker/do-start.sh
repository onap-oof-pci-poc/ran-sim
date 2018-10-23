#!/bin/bash

# Script to configure and start the Policy components that are to run in the designated container,
# It is intended to be used as the entrypoint in the Dockerfile, so the last statement of the
# script just goes into a long sleep so that the script does not exit (which would cause the
# container to be torn down).

container=$1

case $container in
ransim)
	comps="base ransim mysql"
	;;
*)
	echo "Usage: do-start.sh ransim" >&2
	exit 1
esac


# skip installation if build.info file is present (restarting an existing container)
if [[ -f /opt/app/policy/etc/build.info ]]; then
	echo "Found existing installation, will not reinstall"
	. /opt/app/policy/etc/profile.d/env.sh

else
	if [[ -d config ]]; then
		cp config/*.conf .
	fi

	for comp in $comps; do
		echo "Installing component: $comp"
		./docker-install.sh --install $comp
	done
	for comp in $comps; do
		echo "Configuring component: $comp"
		./docker-install.sh --configure $comp
	done

	. /opt/app/policy/etc/profile.d/env.sh

	# install policy keystore
	mkdir -p $POLICY_HOME/etc/ssl
	cp config/policy-keystore $POLICY_HOME/etc/ssl

	if [[ -f config/$container-tweaks.sh ]] ; then
		# file may not be executable; running it as an
		# argument to bash avoids needing execute perms.
		bash config/$container-tweaks.sh
	fi

	if [[ $container == ransim ]]; then
		# wait for DB up
		./wait-for-port.sh mariadb 3306
	fi

fi

policy.sh start


sleep 1000d
