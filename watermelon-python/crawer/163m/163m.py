import urllib 
import requests

url = 'http://music.163.com/song/media/outer/url?id=502789965.mp3'  
print("downloading with urllib")
urllib.request.urlretrieve(url, "547059257.mp3")
