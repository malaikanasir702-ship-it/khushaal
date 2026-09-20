const app = require('./app');
const env = require('./config/env');
const { connectDB } = require('./config/db');

const startServer = async () => {
  await connectDB();
  app.listen(env.PORT, () => {
    console.log(`[Khushhaal API] Server running on http://localhost:${env.PORT} in ${env.NODE_ENV} mode`);
  });
};

if (process.env.NODE_ENV !== 'test') {
  startServer();
}

module.exports = app;
