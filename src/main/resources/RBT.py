
# coding: utf-8

# In[164]:


import os
import sys
import apiai
import json
from random import randint
import vk 
import requests
import ast
import time


# In[165]:


def get_session_id(n):
    range_start = 10**(n-1)
    range_end = (10**n)-1
    return randint(range_start, range_end)


# In[166]:


def load_sessions():
    with open('/Users/ruslangm/Desktop/current_sessions.txt', 'r') as fout:
        s = json.loads(fout.read())
        return s


# In[167]:


def save_sessions(current_sessions):
    with open('/Users/ruslangm/Desktop/current_sessions.txt', 'w') as file:
        file.write(json.dumps(current_sessions))


# In[183]:


def do_iteration(user_id, input_message, current_sessions, ai, session, api):

    new_session = True
    now = int(round(time.time()))
    user_id = str(user_id)

    try:
        last_call = current_sessions[user_id][0]
        elapsed = now - last_call
        if elapsed <= 1800:
            new_session = False
        current_sessions[user_id] = [now, current_sessions[user_id][1]]
    except KeyError:
        current_sessions[user_id] = [now, get_session_id(16)]

    session_id = current_sessions[user_id][1]
    
    
    request = ai.text_request()
    request.lang = 'ru'
    request.session_id = session_id
    request.query = input_message
    response = json.loads(request.getresponse().read().decode('utf-8'))
    message = response['result']['fulfillment']['speech']
    
    api.messages.send(user_id=int(user_id), message=message)
    
    return current_sessions


# In[184]:


def run(user_id, input_message):
    CLIENT_ACCESS_TOKEN = '93506ef470c645b0bbafdedbb5e093c4'
    community_token = 'ea66c01ca53fff63ee58607016b40e52af473a521e2b12ee1f19770f0213a41e5c23660b3fb438fe3ac2a'

    ai = apiai.ApiAI(CLIENT_ACCESS_TOKEN)
    session = vk.Session(community_token)
    api = vk.API(session, v='5.35', lang='ru', timeout=10)

    current_sessions = load_sessions()
    current_sessions = do_iteration(user_id, input_message, current_sessions, ai=ai, session=session, api=api)
    save_sessions(current_sessions)


# In[185]:

for line in sys.stdin:
    line = line.strip().split(',')
    run(line[0], str(line[1]))


