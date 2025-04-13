CREATE SCHEMA IF NOT EXISTS gallerydb;
USE gallerydb;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
                       id INT AUTO_INCREMENT PRIMARY KEY,  -- Auto increment the user ID
                       full_name VARCHAR(100) NOT NULL,    -- Store the user's full name
                       email VARCHAR(100) NOT NULL UNIQUE, -- Store the user's email and ensure it's unique
                       password VARCHAR(255) NOT NULL,     -- Store the user's password (hashed, not plain text)
                       role INT DEFAULT 1 NOT NULL,        -- User role: 1=regular user (default), 0=admin, 2=super admin
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- Track when the user was created
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Track when user details were last updated
);

-- Create the gallery_items table with a BLOB column to store images
CREATE TABLE IF NOT EXISTS gallery_items (
                               id INT AUTO_INCREMENT PRIMARY KEY,   -- Auto increment the gallery item ID
                               title VARCHAR(255) NOT NULL,          -- Store the title of the gallery item
                               image MEDIUMBLOB,                     -- Store the image as binary data (up to 16MB)
                               user_id INT,                          -- Foreign key to link the gallery item to a user
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- Track when the gallery item was created
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Track when gallery item was last updated
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Ensure gallery items are deleted if the user is deleted
);

-- Create the activity_logs table to track user activities
CREATE TABLE IF NOT EXISTS activity_logs (
                              id INT AUTO_INCREMENT PRIMARY KEY,   -- Auto increment the activity log ID
                              activity VARCHAR(255) NOT NULL,       -- Description of the activity
                              activity_type VARCHAR(50),            -- Type of activity (upload, delete, login, etc.)
                              user_id INT,                          -- Foreign key to link the activity to a user
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- Track when the activity occurred
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Ensure logs are deleted if the user is deleted
);

CREATE TABLE IF NOT EXISTS activity_logs (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              activity VARCHAR(255) NOT NULL,
                              activity_type VARCHAR(100),
                              user_id INT NOT NULL,
                              user_name VARCHAR(100),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id)
                                      ON DELETE CASCADE
                                      ON UPDATE CASCADE
);
-- Create the tags table
CREATE TABLE IF NOT EXISTS tags (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(50) NOT NULL UNIQUE,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create the image_tags relationship table
CREATE TABLE IF NOT EXISTS image_tags (
                                          image_id INT NOT NULL,
                                          tag_id INT NOT NULL,
                                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          PRIMARY KEY (image_id, tag_id),
                                          FOREIGN KEY (image_id) REFERENCES gallery_items(id) ON DELETE CASCADE,
                                          FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- Create index for faster lookups
CREATE INDEX idx_image_tags_image_id ON image_tags(image_id);
CREATE INDEX idx_image_tags_tag_id ON image_tags(tag_id);
