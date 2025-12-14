package com.dsa.data;

import com.dsa.core.Track;
import java.util.*;

public class SpotifyService {
        private List<Track> library;
        private List<Track> userHistory;

        public SpotifyService() {
                library = new ArrayList<>();
                userHistory = new ArrayList<>();
                loadMockData();
        }

        private void loadMockData() {
                // --- ENGLISH POP (High Energy) ---
                addTrack("101", "Blinding Lights", "The Weeknd", "Pop", 0.9, 0.5,
                                "https://i.scdn.co/image/ab67616d0000b2738863bc11d2aa12b54f5aeb36");
                addTrack("102", "Levitating", "Dua Lipa", "Pop", 0.8, 0.9,
                                "https://i.scdn.co/image/ab67616d0000b27329f6de9c122f085871f3b392");
                addTrack("103", "Shape of You", "Ed Sheeran", "Pop", 0.7, 0.8,
                                "https://i.scdn.co/image/ab67616d0000b273ba5db46f4b838ef6027e6f96");
                addTrack("104", "As It Was", "Harry Styles", "Pop", 0.75, 0.6,
                                "https://i.scdn.co/image/ab67616d0000b273b46f74097655d7f353caab14");

                // --- HINDI (Bollywood / Indie) ---
                addTrack("201", "Kesariya", "Arijit Singh", "Hindi", 0.6, 0.7,
                                "https://i.scdn.co/image/ab67616d0000b273c08202c503d57f7222dfc9c1");
                addTrack("202", "Apna Bana Le", "Arijit Singh", "Hindi", 0.4, 0.3,
                                "https://i.scdn.co/image/ab67616d0000b2731a575ead6375c324dc8c7409");
                addTrack("203", "Besharam Rang", "Vishal-Shekhar", "Hindi", 0.8, 0.7,
                                "https://i.scdn.co/image/ab67616d0000b27390820a4005b82b3dc922646d");
                addTrack("204", "Maan Meri Jaan", "King", "Hindi Pop", 0.6, 0.5,
                                "https://i.scdn.co/image/ab67616d0000b27301c23178a9c27771de996376");
                addTrack("205", "Chaleya", "Arijit Singh", "Hindi", 0.75, 0.8,
                                "https://i.scdn.co/image/ab67616d0000b273c524855490076a44b5dd55aa");

                // --- PUNJABI (High Energy / Hype) ---
                addTrack("301", "Brown Munde", "AP Dhillon", "Punjabi", 0.85, 0.7,
                                "https://i.scdn.co/image/ab67616d0000b2733f3d35091bc7df1531e0f09f");
                addTrack("302", "Excuses", "AP Dhillon", "Punjabi", 0.6, 0.4,
                                "https://i.scdn.co/image/ab67616d0000b27300c14f33230b8098555e8869");
                addTrack("303", "Elevated", "Shubh", "Punjabi", 0.8, 0.6,
                                "https://i.scdn.co/image/ab67616d0000b273f55074251f23788a44ec4cc0");
                addTrack("304", "The Last Ride", "Sidhu Moose Wala", "Punjabi", 0.7, 0.5,
                                "https://i.scdn.co/image/ab67616d0000b27318048d613e157e33554e5e40");
                addTrack("305", "Lover", "Diljit Dosanjh", "Punjabi", 0.75, 0.85,
                                "https://i.scdn.co/image/ab67616d0000b273cd944a938c227856350314b9");

                // --- URDU / PAKISTANI POP ---
                addTrack("401", "Pasoori", "Ali Sethi x Shae Gill", "Urdu Pop", 0.7, 0.6,
                                "https://i.scdn.co/image/ab67616d0000b273a55424df4db73cb37e6f9661");
                addTrack("402", "Kahani Suno 2.0", "Kaifi Khalil", "Urdu", 0.3, 0.2,
                                "https://i.scdn.co/image/ab67616d0000b2731d1cc2e40d533d7bcebf5dae");
                addTrack("403", "Peechay Hutt", "Hasan Raheem", "Urdu Pop", 0.8, 0.7,
                                "https://i.scdn.co/image/ab67616d0000b2734a7493a388db4cd9630c8854");
                addTrack("404", "Iraaday", "Abdul Hannan", "Urdu R&B", 0.4, 0.4,
                                "https://i.scdn.co/image/ab67616d0000b273fa48873fb3981881ce467a84");

                // --- K-POP (Korean) ---
                addTrack("501", "Dynamite", "BTS", "K-Pop", 0.9, 0.9,
                                "https://i.scdn.co/image/ab67616d0000b27329868d4586c8d626388e6583");
                addTrack("502", "Pink Venom", "BLACKPINK", "K-Pop", 0.85, 0.6,
                                "https://i.scdn.co/image/ab67616d0000b273a1103c809637d94943775f0a");
                addTrack("503", "Hype Boy", "NewJeans", "K-Pop", 0.7, 0.8,
                                "https://i.scdn.co/image/ab67616d0000b2739d28fd018590e0513758064a");
                addTrack("504", "Butter", "BTS", "K-Pop", 0.8, 0.9,
                                "https://i.scdn.co/image/ab67616d0000b273e9702213824b0b830d1de72f");
                addTrack("505", "OMG", "NewJeans", "K-Pop", 0.75, 0.75,
                                "https://i.scdn.co/image/ab67616d0000b273d70036292d54f29e8b68ec01");

                // --- LOFI / CHILL (Low Energy) ---
                addTrack("601", "Heather", "Conan Gray", "Acoustic", 0.3, 0.2,
                                "https://i.scdn.co/image/ab67616d0000b273c5cb637452d5fb83cc59b8be");
                addTrack("602", "Night Trouble", "Petit Biscuit", "Chill", 0.4, 0.3,
                                "https://i.scdn.co/image/ab67616d0000b2731885661b369c735d8e7dcbb2");
        }

        private void addTrack(String id, String name, String artist, String genre, double energy, double valence,
                        String img) {
                library.add(new Track(id, name, artist, genre, energy, valence, img));
        }

        public List<Track> getAllTracks() {
                return library;
        }

        public List<Track> getUserHistory() {
                return userHistory;
        }

        public void addToHistory(Track t) {
                userHistory.add(t);
        }
}
