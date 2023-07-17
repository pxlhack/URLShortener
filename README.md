# URL Shortener

## Prerequisites

Make sure you have the following installed on your machine:

- Docker:
- Docker Compose:

## Getting Started

1. Clone the repository to your local machine
    ```bash
    git clone https://github.com/pxlhack/URLShortener.git
    ```
2. Navigate to the project directory.
   ```bash
    cd URLShortener
    ```
3. Build and start the containers.
   ```bash
    docker-compose up -d
    ```
   This command will build the necessary images and start the containers in detached mode. You should see the output
   indicating the successful creation and startup of the services.
4. Verify the service is running.
   ```bash
    docker-compose ps
    ```
   This command will display the status of the containers. Ensure that all containers are in the "Up" state.

5. Access the service. <br>
   The service should now be accessible at the specified port or URL, depending on your configuration. Open your
   preferred web browser and navigate to the appropriate address to use the service.

## Stopping the Service

To stop the running containers, use the following command:

   ```bash
  docker-compose down
   ```

This command will stop and remove the containers, but it will retain any data stored in volumes. If you want to remove
the volumes as well, add the **--volumes** flag:

   ```bash
  docker-compose down -volumes

   ```
