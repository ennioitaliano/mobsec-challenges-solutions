# 1. filehasher
Since the challenge explicitly states to define an activity with the intent filter for `com.mobiotsec.intent.action.HASHFILE`, I just added it to the main activity in the manifest, but I was still getting the `ActivityNotFoundException` message. 

By looking with more attention at the error message I saw that I was missing the `data` and `category` elements to proper handle the incoming intent - once added (as per error message), the victim app's activity is displayed correctly instead of crashing right away.

Then, I put the code needed to intercept the intent in the `onCreate` function of my app's `MainActivity`, and got a file URI: 
```content://com.example.victimapp.fileprovider/data/YM3oPnYG.dat```

I hashed its contents and put the hashed result in a result intent, ran the victim app again and correctly saw the flag in the logs.

## Victim app's security issues
- Explicit intent with app signature check would've been more secure
- Also another component with custom permission and signature protection level would've done
- Victim app relied on a flag that gave write access to its files to external apps; that's why the attacker could read (only once due to the flag functioning) the .dat file in the internal storage of the victim
