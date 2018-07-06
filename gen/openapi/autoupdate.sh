#!/bin/bash

# Copyright 2017 The Kubernetes Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Script to fetch latest swagger spec.
# Puts the updated spec at api/swagger-spec/

set -o errexit
set -o nounset
set -o pipefail

# Run this script on the root of the repo to automatically
# detect the language and update its client. Assumptions are:
# - the repo name is the language name.
# - the output folder is named "kubernetes" at the root of the repo
# - setting file is named "settings" at the root of the repo

SCRIPT_ROOT=$(dirname "${BASH_SOURCE}")
DIR_ROOT=$(pwd)
DIR_NAME=${PWD##*/}

echo "Running command \"${SCRIPT_ROOT}/${DIR_NAME}.sh\" \"${DIR_ROOT}/kubernetes\" \"${DIR_ROOT}/settings\""

"${SCRIPT_ROOT}/${DIR_NAME}.sh" "${DIR_ROOT}/kubernetes" "${DIR_ROOT}/settings"


