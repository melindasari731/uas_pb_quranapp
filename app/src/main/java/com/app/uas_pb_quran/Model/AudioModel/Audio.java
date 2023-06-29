package com.app.uas_pb_quran.Model.AudioModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Audio{

	@SerializedName("audio_files")
	private List<AudioFilesItem> audioFiles;

	public List<AudioFilesItem> getAudioFiles(){
		return audioFiles;
	}

	@Override
 	public String toString(){
		return 
			"Audio{" + 
			"audio_files = '" + audioFiles + '\'' + 
			"}";
		}
}