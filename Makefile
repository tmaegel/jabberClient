COMPILER = gradle
AUTHOR = tmaegel
BUILDPATH = ./build/outputs/apk
NAME = jabberClient
PKG = com.$(AUTHOR).$(NAME)
ACTIVITY = MainActivity
TYPE = debug-unaligned
APK = $(NAME)-$(TYPE).apk
INSTALL = $(BUILDPATH)/$(APK)

build: clean
	$(COMPILER) build

install: $(INSTALL)
	adb logcat -c
	adb shell am kill com.tmaegel.jabberClient
	adb install -r $(INSTALL)
	adb shell am start -n $(PKG)/$(PKG).$(ACTIVITY)
	adb logcat $(NAME):* *:S

all: clean build install

clean:
	rm -f $(BUILDPATH)/*.apk
