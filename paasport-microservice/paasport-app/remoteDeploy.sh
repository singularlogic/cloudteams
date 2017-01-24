export PROD_SERVER=x.x.x.x
export USER=xxx
echo "Starting deployment on Production Server ($USER@$PROD_SERVER)"
echo "Deploying..."
#ssh $USER@$PROD_SERVER '~/deploy.sh'
echo "Deployment Done!"
