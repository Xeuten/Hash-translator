package tt.hashtranslator.model;

/**
 * The status classifies into three states: "PROCESSING" (the initial state), "PROCESSED" (corresponds to the
 * situation when the application was processed and not all the hashes were decrypted), "DECRYPTED" (corresponds to the
 * situation when the application was processed and all the hashes were decrypted).
 */
public enum DecryptApplicationStatus { PROCESSING, PROCESSED, DECRYPTED }
