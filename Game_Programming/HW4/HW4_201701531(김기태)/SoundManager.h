// SoundManager.h
//

#ifndef SOUNDMANAGER_H
#define SOUNDMANAGER_H

//-----------------------------------------------------------------------------
// Header Includes
//-----------------------------------------------------------------------------
#include <dsound.h>  //library "dsound.lib"
//#include <dxerr.h> //library "dxerr.lib" for DXTRACE_ERR
//No dxerr.lib -- jong -- temporary
//#include <dxerr.h>
//#define DXTRACE_ERR(str,hr) DXTrace(__FILE__,(DWORD)__LINE__,hr,str, FALSE)
#define DXTRACE_ERR(str,hr) E_FAIL //jong -- temporary

//-----------------------------------------------------------------------------
//입력값을 dB(decibel)로 바꿔주는 매크로 함수

//DirectSound에서 볼륨단위인 dB(decibel)은 0값이 최대 볼륨이고, -10000이 무음이다.
//우리가 일반적으로 생각하는 볼륨값이 아니므로, 0~100까지의 값을 넘겨주면 dB로
//변환 시켜주는 매크로 함수 DSVOLUME_TO_DB(volume)를 만들었다.
//-----------------------------------------------------------------------------
//#define DSVOLUME_TO_DB(volume)   ((DWORD)(-30*(100-volume))) 
#define DSVOLUME_TO_DB(volume)   ((DWORD)(-50*(100-volume)))


//#pragma comment (lib, "dxerr.lib")
#pragma comment (lib, "dsound.lib")

#include <vector>

//-----------------------------------------------------------------------------
// Classes used by this header
//-----------------------------------------------------------------------------
class CSoundManager;
class CSound;
class CStreamingSound;
class CWaveFile;


//-----------------------------------------------------------------------------
// Typing macros 
//-----------------------------------------------------------------------------
#define DXUT_StopSound(s)         { if(s) s->Stop(); }
#define DXUT_PlaySound(s)         { if(s) s->Play( 0, 0 ); }
#define DXUT_PlaySoundLooping(s)  { if(s) s->Play( 0, DSBPLAY_LOOPING ); }


//-----------------------------------------------------------------------------
// Name: class CSoundManager
// Desc: 
//-----------------------------------------------------------------------------
class CSoundManager
{
protected:
	IDirectSound8* m_pDS;
private:
	std::vector <CSound*> m_soundVector;

public:
	// 볼륨 조절을 위함
	IDirectSoundBuffer* pDSBPrimary;

	CSoundManager();
	~CSoundManager();

	bool init(HWND hWnd);
	void release();
	void stop(int id);
	bool play(int id, bool bLooped);
	bool add(LPTSTR filename, int* id);

	HRESULT Initialize(HWND hWnd, DWORD dwCoopLevel);
	inline IDirectSound8* GetDirectSound() { return m_pDS; }
	HRESULT SetPrimaryBufferFormat(DWORD dwPrimaryChannels, DWORD dwPrimaryFreq, DWORD dwPrimaryBitRate);
	HRESULT Get3DListenerInterface(IDirectSound3DListener** ppDSListener);

	HRESULT Create(CSound** ppSound, LPTSTR strWaveFileName, DWORD dwCreationFlags = 0, GUID guid3DAlgorithm = GUID_NULL, DWORD dwNumBuffers = 1);
	HRESULT CreateFromMemory(CSound** ppSound, BYTE* pbData, ULONG ulDataSize, LPWAVEFORMATEX pwfx, DWORD dwCreationFlags = 0, GUID guid3DAlgorithm = GUID_NULL, DWORD dwNumBuffers = 1);
	HRESULT CreateStreaming(CStreamingSound** ppStreamingSound, LPTSTR strWaveFileName, DWORD dwCreationFlags, GUID guid3DAlgorithm, DWORD dwNotifyCount, DWORD dwNotifySize, HANDLE hNotifyEvent);
};


//-----------------------------------------------------------------------------
// Name: class CSound
// Desc: Encapsulates functionality of a DirectSound buffer.
//-----------------------------------------------------------------------------
class CSound
{
protected:
	IDirectSoundBuffer** m_apDSBuffer;
	DWORD                m_dwDSBufferSize;
	CWaveFile* m_pWaveFile;
	DWORD                m_dwNumBuffers;
	DWORD                m_dwCreationFlags;

	HRESULT RestoreBuffer(IDirectSoundBuffer* pDSB, BOOL* pbWasRestored);

public:
	CSound(IDirectSoundBuffer** apDSBuffer, DWORD dwDSBufferSize, DWORD dwNumBuffers, CWaveFile* pWaveFile, DWORD dwCreationFlags);
	virtual ~CSound();

	HRESULT Get3DBufferInterface(DWORD dwIndex, IDirectSound3DBuffer** ppDS3DBuffer);
	HRESULT FillBufferWithSound(IDirectSoundBuffer* pDSB, BOOL bRepeatWavIfBufferLarger);
	IDirectSoundBuffer* GetFreeBuffer();
	IDirectSoundBuffer* GetBuffer(DWORD dwIndex);

	HRESULT Play(DWORD dwPriority = 0, DWORD dwFlags = 0, LONG lVolume = 0, LONG lFrequency = -1, LONG lPan = 0);
	HRESULT Play3D(LPDS3DBUFFER p3DBuffer, DWORD dwPriority = 0, DWORD dwFlags = 0, LONG lFrequency = 0);
	HRESULT Stop();
	HRESULT Reset();
	BOOL    IsSoundPlaying();
};


//-----------------------------------------------------------------------------
// Name: class CStreamingSound
// Desc: Encapsulates functionality to play a wave file with DirectSound.  
//       The Create() method loads a chunk of wave file into the buffer, 
//       and as sound plays more is written to the buffer by calling 
//       HandleWaveStreamNotification() whenever hNotifyEvent is signaled.
//-----------------------------------------------------------------------------
class CStreamingSound : public CSound
{
protected:
	DWORD m_dwLastPlayPos;
	DWORD m_dwPlayProgress;
	DWORD m_dwNotifySize;
	DWORD m_dwNextWriteOffset;
	BOOL  m_bFillNextNotificationWithSilence;

public:
	CStreamingSound(IDirectSoundBuffer* pDSBuffer, DWORD dwDSBufferSize, CWaveFile* pWaveFile, DWORD dwNotifySize);
	~CStreamingSound();

	HRESULT HandleWaveStreamNotification(BOOL bLoopedPlay);
	HRESULT Reset();
};


//-----------------------------------------------------------------------------
// Typing macros 
//-----------------------------------------------------------------------------
#define WAVEFILE_READ   1
#define WAVEFILE_WRITE  2

//-----------------------------------------------------------------------------
// Name: class CWaveFile
// Desc: Encapsulates reading or writing sound data to or from a wave file
//-----------------------------------------------------------------------------
class CWaveFile
{
public:
	WAVEFORMATEX* m_pwfx;        // Pointer to WAVEFORMATEX structure
	HMMIO         m_hmmio;       // MM I/O handle for the WAVE
	MMCKINFO      m_ck;          // Multimedia RIFF chunk
	MMCKINFO      m_ckRiff;      // Use in opening a WAVE file
	DWORD         m_dwSize;      // The size of the wave file
	MMIOINFO      m_mmioinfoOut;
	DWORD         m_dwFlags;
	BOOL          m_bIsReadingFromMemory;
	BYTE* m_pbData;
	BYTE* m_pbDataCur;
	ULONG         m_ulDataSize;
	CHAR* m_pResourceBuffer;

protected:
	HRESULT ReadMMIO();
	HRESULT WriteMMIO(WAVEFORMATEX* pwfxDest);

public:
	CWaveFile();
	~CWaveFile();

	HRESULT Open(LPTSTR strFileName, WAVEFORMATEX* pwfx, DWORD dwFlags);
	HRESULT OpenFromMemory(BYTE* pbData, ULONG ulDataSize, WAVEFORMATEX* pwfx, DWORD dwFlags);
	HRESULT Close();

	HRESULT Read(BYTE* pBuffer, DWORD dwSizeToRead, DWORD* pdwSizeRead);
	HRESULT Write(UINT nSizeToWrite, BYTE* pbData, UINT* pnSizeWrote);

	DWORD   GetSize();
	HRESULT ResetFile();
	WAVEFORMATEX* GetFormat() { return m_pwfx; };
};


#endif // SOUNDMANAGER_H

