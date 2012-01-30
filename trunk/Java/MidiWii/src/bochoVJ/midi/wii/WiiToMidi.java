package bochoVJ.midi.wii;

import javax.sound.midi.InvalidMidiDataException;

import bochoVJ.midi.MidiManagerOut;
import bochoVJ.wii.IWiiHandler;
import bochoVJ.wii.IWiiManager;
import bochoVJ.wii.WiiRemoteJManager;
import bochoVJ.wii.WiiuseJManager;
import bochoVJ.wii.IWiiHandler.Acceleration;

public class WiiToMidi {

	public MidiManagerOut midiOut;

	public IWiiManager wiimng;

	int mididevice;

	Configuration config;

	private boolean muteAccX;
	private boolean muteAccY;
	private boolean muteAccZ;

	public Configuration getCurrentConfig()
	{
		return config;
	}

	public WiiToMidi(int mididevice)
	{
		muteAccX = false;
		muteAccY = false;
		muteAccZ = false;

		config = Configuration.defaultConfiguration();

		this.mididevice = mididevice;

		midiOut = new MidiManagerOut();

		//wiimng = new WiiRemoteJManager();
		wiimng = new WiiuseJManager();


		wiimng.addHandler(new IWiiHandler() {

			@Override
			public void handleButton(WiiButton b) {

				try {
					midiOut.sendNoteOn(config.buttonsChannel, config.getButtonNote(b), 100);
				} 
				catch (InvalidMidiDataException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void handleAcc(Acceleration acc) {
				try{
					if(!muteAccX)
						midiOut.sendControlChange(config.accsChannel, config.accx, accToControl(acc.x));

					if(!muteAccY)
						midiOut.sendControlChange(config.accsChannel, config.accy, accToControl(acc.y));

					if(!muteAccZ)
						midiOut.sendControlChange(config.accsChannel, config.accz, accToControl(acc.z));
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
	}


	public void muteAccX(boolean muted)
	{
		muteAccX = muted;
	}

	public void muteAccY(boolean muted)
	{
		muteAccY = muted;
	}

	public void muteAccZ(boolean muted)
	{
		muteAccZ = muted;
	}


	private int accToControl(double v)
	{
		int val = (int)(Math.abs(v)  * 127/ Acceleration.MaxACC) ;
		if(val >127)
			val = 127;

		return val;
	}

	public void start() throws Exception
	{
		System.out.println("Starting midi out and wii");

		midiOut.startDevice(mididevice);

		wiimng.connect();
	}

	public void stop()
	{
		System.out.println("Stopping midi out and wii");

		wiimng.disconnect();

		midiOut.close();
	}

	public void configure(Configuration c)
	{
		config = c;
	}
}