> bitrise.yml
curl -X GET "https://api.bitrise.io/v0.1/apps/$1/bitrise.yml" -H "Authorization: $2" > bitrise.yml

if ! git diff-index --quiet HEAD --; then
    git add bitrise.yml
    git commit -m "updating bitrise.yml from build #$3"
    git push
fi