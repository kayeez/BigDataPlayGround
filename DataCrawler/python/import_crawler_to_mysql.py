import pandas as pd

if __name__ == "__main__":
    # Define the path to your JSON file
    json_file_path = json_file_path = os.path.join("D:", "github-repos", "BigDataPlayGround", "BigDataPlayGround", "DataCrawler", "data", "小红书 - 你的生活指南.json")

    # Read the JSON file into a DataFrame
    df = pd.read_json(json_file_path, orient='index').transpose()

    # Database connection details
    db_config = {
        'user': 'hadoop',
        'password': 'Kayee@19921024',
        'host': '192.168.31.42',
        'database': 'warehouse_raw'
    }

    # Create a connection to the MySQL database
    connection = mysql.connector.connect(**db_config)

    # Create an SQLAlchemy engine
    engine = create_engine(f"mysql+mysqlconnector://{db_config['user']}:{db_config['password']}@{db_config['host']}/{db_config['database']}")

    # Specify the table name where data will be inserted
    table_name = 'your_table_name'

    # Write the DataFrame to the MySQL table
    df.to_sql(name=table_name, con=engine, if_exists='append', index=False)

    # Close the database connection
    connection.close()