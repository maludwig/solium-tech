# Technical Interview: Linux Administration

Want to get excited? Try my colored bash! https://github.com/maludwig/bashrc

Fun things:
- rsync-sudo: Copy files and directories as root
- rsync-limited: Copy slowly, other people are using this network connection, and you love them.
- gitgraph: Visually show git commits
- msg-success: Green echo!
- mark & back: Mark a directory, and then quickly go back to it.
- flick: Go to the marked directory, typing it again will flick you back.
- yumlg, aglg, piplg: Search for a package
- sr: Restart a service
- mst-time: Set the timezone to Mountain Time
- prepend: Put a string at the top of a file
- ensure_running_agent: Start the ssh-agent unless it's already running
- generate-ssh-key: Remembering all this is hard.

Full list with no documentation is at:
https://github.com/maludwig/bashrc/blob/master/aliases

## 1. What is your favourite unix command line editor?

Vim.

You want to get in a real fight though, tell me, to my face, the scp is better than rsync. But we'll get to that.

## 2. Please give a description of the following unix commands and give examples of how they would be used on the command line and specify some of the options

### a. pwd

pwd Prints Working Directory.
I use this essentially when I want to remember the current directory at the beginning of a bash script that cd's elsewhere, and needs to come back. It's also nice for getting the "actual" path for rsync/scp.

Example:
```bash
#!/bin/bash
INITIAL_DIR=`pwd`
# Do stuff...
cd "$INITIAL_DIR"
```

Options:

...pwd has command line options? \*man pwd\* huh.

- -L, --logical: Use symlinks
- -P, --physical: Avoid symlinks
- --version: Literally nobody has ever needed to use this option. Linus probably never wrote code to handle it.

### b. ln

ln is for making links, hardlinks and symlinks. Best piece of advice I've ever gotten is "use it like cp" for remembering the order of things.

Example:
```bash
# Set timezone to Mountain
ln -fs /usr/share/zoneinfo/America/Edmonton /etc/localtime
```
- -f, --force: I don't care that there's a file there already.
- -s, --symbolic: Make a symlink instead of a hard link

### c. ant

ant is for building projects. It looks for your XML build file and does the things that the buildfile wants done. It can do arbitrary things, but generally it's used by developers when making projects:

Example:
```bash
ant clean
ant generate-javadoc
ant build-jar
```
The options are generally put into a complex "build.xml" file that defines operations and operation dependencies.

### d. sed

sed is amazing. sed might be my favorite right after grep. Never use sed without -r and a single quote. It's different on Ubuntu and on RHEL in ways I've never bothered to figure out if you don't use -r.

Example:
```bash
# Wipe my password from my bash history
sed -i -r 's/My100%SecurePassword/\*\*\*\*/g' ~/.bash_history
```
Options:
- -r, --regexp-extended: Use actual Regular Expressions
- -i[SUFFIX], --in-place[=SUFFIX]: Use in-place file replacements, and make a backup with the suffix, if given.
- -u, --unbuffered: For when you want sed to flush buffers regularly, instead of waiting for the end. Handy with chaining sed with something like "tail -f".

### e. chmod

Modifies file/directory permissions. Linux calls this the "file mode" to mess with Windows people. It also uses a capital -R for recursion because the person who made chmod and chown didn't want to follow the lowercase -r standard for everything else, like, grep, diff, scp, etc.

Example:
```bash
# Set proper permissions on ~/.ssh folder
cd ~/.ssh
chmod 600 *
chmod 700 .

# Make a script
vim my-script.sh
chmod u+x my-script.sh
./my-script.sh
```

Options:
- -R, --recursive: Change all files and folders recursively.

### f. ps

Prints currently running processes. Almost unconditionally chained with a grep, otherwise you'd be using top.

Example:
```bash
# Find the java process with the fattest maximum heap
# And yes, I wish sort -h would handle lowercase suffixes properly

ps aux | grep java | sed -r 's/^(.*)(Xmx)([^ ]+)g/\3,000m\t\1\2\3/' | sed -r 's/^(.*)(Xmx)([^ ]+)m/\3m\t\1\2\3/' | sort -r -n | head -n 1
```

### g. kill

MURDER AN EVIL PROCESS. Technically you can send a variety of signals, and, fun fact, many services use this to send a "reload your configs" command. Normal humans basically use it like:

Example:
```bash
ps aux | grep java
kill 1234
ps aux | grep java
kill -9 1234

vim /etc/httpd/conf.d/ssl.conf
killall -HUP httpd
```

Options:
- -9: I told you to die. I was nice. You didn't die. Now. You. DIE.
- -HUP: Actually just reload your config, plz.

### h. scp

The copy command used by people who haven't heard of rsync. Slow. No compression. Can't override the user. Sucks in all ways.

Example copying configs with scp:
```bash
# Start optimistic
mitchell@mitchellslaptop: ~/: scp -r ~/config-repo/httpd/api-server/etc/httpd api49tor.solium.com:/etc/httpd
# Maybe there's an option...
mitchell@mitchellslaptop: ~/: man scp
# Ok, we'll change the permissions then
mitchell@mitchellslaptop: ~/: ssh api49tor.solium.com
mitchell@api49tor: ~/: cd /etc/httpd
mitchell@api49tor: /etc/httpd: chmod 700 -R /etc/httpd
# Right, duh
mitchell@api49tor: /etc/httpd: sudo chmod 700 -R /etc/httpd
mitchell@api49tor: /etc/httpd: sudo chown mitchell:wheel -R /etc/httpd
mitchell@api49tor: /etc/httpd: exit
# Round 2, next time I'll use /tmp
mitchell@mitchellslaptop: ~/: scp -r ~/config-repo/httpd/api-server/etc/httpd 
# Hopefully that worked
mitchell@mitchellslaptop: ~/: ssh api49tor.solium.com
mitchell@api49tor: ~/: cd /etc/httpd/conf.d
mitchell@api49tor: /etc/httpd/conf.d/: cat new-config.conf
mitchell@api49tor: /etc/httpd/conf.d/: exit
```

Example copying configs with rsync:
```bash
# Very handy alias for being root on the other side
alias rsync-sudo='rsync -e "ssh" --progress --rsync-path="sudo rsync"'
# Just works
rsync-sudo -av ~/config-repo/httpd/api-server/etc/httpd api49tor.solium.com:/etc/httpd
```

Options:
- -C: Enable the compression algorithm that honestly barely helps at all. Pretty sure this switch is just ignored.
- -i: Use a specific private key
- -r: Recursive mode

### i. ssh

The best, most beautiful remote control program ever written. Completely secure. Supports all kinds of encryption. Supports port-forwarding, securely. Runs single commands remotely, runs terminals, is super light, persists through flaky networks. Empowers sftp, scp, rsync, nagios...oh man, so many things. This is the most amazing program that will ever be written by men or gods.

Best used with an SSH Agent, otherwise your end users won't leave the password on their private keys, and SSH Agent forwarding won't work.

Examples:
```bash
mitchell@mitchellslaptop: ~/: ssh server.domainname.com
mitchell@mitchellslaptop: ~/: ssh server.domainname.com -- echo '$HOSTNAME'
mitchell@mitchellslaptop: ~/: ssh -i ~/.ssh/RootKeyFileBecauseIUseSCPAndIGotAnnoyed.pem root@server.domainname.com

# Agent forwarding is awesome
mitchell@mitchellslaptop: ~/: ssh -A bastion.solium.com
mitchell@bastion: ~/: ssh api49tor.solium.com
mitchell@api49tor: ~/: echo $HOSTNAME
```

### j. crontab

Scheduled tasks, great for health checks and log rotations and stuff

Examples:
```bash
crontab -l
crontab -e
```

Options:
- -l: List jobs
- -e: Edit the jobs

### k. wc

Word count. Never used without -l switch, except by English majors who want the word count of their essay that they've written in vim because they are deeply weird English majors.

```bash
cd /var/log/solium/webserver
cat error-log
grep '2017-10-25T17:' error-log | grep '/auth/login' | wc -l
```

Options:
- -l: Count the lines.

## 3. How would you accomplish the following tasks on the command line?

### a. Switch to the Super User to perform administrative functions

```bash
sudo su
```

### b. Get the last 500 lines of a log file

```bash
tail -n /var/log/httpd/ssl_request_log
```

### c. Compare the differences between two files.

```bash
diff create-server-v1.sh create-server-v2.sh
```

### d. Compare the differences between all files in two directories

```bash
diff -r mycode/ tomscode/
```

### e. Find the process that is using the most CPU

```bash
# Raw shell
ps --no-headers -eo pcpu,pid,tid,class,rtprio,ni,pri,psr,stat,wchan:14,cmd | sort -n | tail -n 1
# Actually tho
htop
yum install htop
htop
```

## 4. Explain what DNS is.

Domain Name Servers "resolve" human-rememberable names into IP addresses, and also offer up certain metadata for things like mailservers. So your DNS server might have records like:

| Record Type | Name | Value | Time To Live |
| ----------- | ---- | ----- | ------------ |
| AAAA | api49tor.solium.com | 4321:0:1:2:3:4:567:89ab | 300 |
| A | solium.com | 12.34.56.78 | 600 |
| CNAME | www.solium.com | solium.com | 3600 |
| NS | solium.com | ns-1234.awsdns-29.org. | 86400 |
| TXT | solium.com | "v=spf1 include:spf.protection.outlook.com ip4:12.34.56.78 ip4:12.34.56.78 -all" | 3600 |
| MX | solium.com | 10 solium-com.mail.protection.outlook.com | 86400 |

AAAA records are for IPv6 entries, A is for IPv4, CNAME is essentially for aliases, NS is for what nameservers are the best, TXT is for configuring random stuff like mail clients, MX is for mailservers to know where to send mail, SRV is for finding services.

All DNS entries are cached for a set time called the "Time To Live" which is very important, particularly if you're moving DNS providers. Set that TTL to like 10 min when you're switching providers, because otherwise you'll just be fretting over whether or not you clicked the right buttons. "nslookup" is your friend too. On Windows, check out "set debug=on"

```shell
nslookup - your-new-nameserver.com
solium.com
set debug=on
solium.com
```

## 5. What is an SSH reverse tunnel and when would you use it?

Oh god. No.

Just use SSH Agent Forwarding with proper public keys and disable password login and a bastion host if you're paranoid or behind NAT. Use a bastion host on a wonky port with no password auth and MFA if you don't trust the security of SSH on every server. (MFA is actually fairly easy to set up.) Please. This is so confusing for end users. But ok, ok, let's go through it.

You have two servers. SECURE1 and OPEN2. Alice is sitting at SECURE1 and Bob is sitting at OPEN2. Bob wants to SSH into SECURE1, but SECURE1 is sitting behind a firewall that blocks port 22, the SSH port. So Bob can't just hop in. So Bob asks Alice, "Can you SSH into OPEN2 instead?" and so Alice opens up a "reverse tunnel" to him, like:
```bash
alice@SECURE1: ~/: ssh -fN -R 2222:localhost:22 alice@OPEN2
```

So now, since OPEN2 doesn't have port 22 blocked by the firewall, Alice has opened a tunnel to SECURE1 that's an open connection, so it just happens to go through the firewall like magic, when Bob types...
```bash
bob@OPEN2: ~/: ssh localhost -p 2222
```
...he will actually log into SECURE1. Then Bob doesn't find this intuitive, and so he asks Alice to help him out, so you set him up with a ~/.ssh/config file with the details in it, which he never backs up. Now Bob's computer is like a really lame bastion host, and if people connect to it on port 7000, it hops into SECURE1...and...like...confuses everyone. The Firewall boys get upset.

I hope whoever reads this agrees with the opinions expressed above. I promise not to complain overtly if the people who like it are higher ranking than me.

## 6. What is the difference between:

### a. Megabytes
- Symbol: MB
- 1000 Kilobytes, or 1 000 000 Bytes

### b. Mebibytes
- Symbol: MiB
- 1024 KiB, or 1 048 576 Bytes

### c. Gigabytes
- Symbol: GB
- 1000 MB, 1 000 000 KB, 1 000 000 000 Bytes

### d. Gibibyes
- Showing results for Gibibytes
- Search instead for Gibibyes

### e. Megabits
- Symbol: Mb or Mbit
- 1000 Kbits, 1 000 000 bits
