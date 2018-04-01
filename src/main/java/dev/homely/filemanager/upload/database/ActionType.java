package dev.homely.filemanager.upload.database;

/**
 * Defines enum types for use with logging FileEvents
 *
 * DELETE:  use when the file is being deleted permanently
 * EDIT:    use when te file is being editted (ie a text file being updated ext)
 * UPLOAD:  use to indicate that the file was just uploaded
 * RESTRICT: use to temporarily block off access to a file but leave it in take on the storage
 * VIEW: viewed the specified file
 */
public enum ActionType {
    DELETE, EDIT, UPLOAD, RESTRICT, VIEW
}
