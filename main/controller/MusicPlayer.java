import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.net.URI;

public class MusicPlayer {

    private static final String clientId = "YOUR_CLIENT_ID";
    private static final String clientSecret = "YOUR_CLIENT_SECRET";

    public static void main(String[] args) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(URI.create("http://localhost:8888/callback"))
                .build();

        String trackId = "3n3Ppam7vgaVa1iaRUc9Lp"; // Example: "Mr. Brightside" by The Killers

        GetTrackRequest getTrackRequest = spotifyApi.getTrack(trackId).build();

        try {
            Track track = getTrackRequest.execute();

            System.out.println("Track Name: " + track.getName());
            System.out.println("Artist: " + track.getArtists()[0].getName());
            System.out.println("Album: " + track.getAlbum().getName());
            System.out.println("Duration (ms): " + track.getDurationMs());
            System.out.println("Preview URL: " + track.getPreviewUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
