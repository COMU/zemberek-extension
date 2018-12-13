---
layout: default
---

## Giriş

LibreOffice Türkçe Dil Araçları eklentisi, LibreOffice için [Zemberek-NLP](https://github.com/ahmetaa/zemberek-nlp) kütüphanesi kullanılarak yazılmış bir Türkçe Yazım Denetimi eklentisidir.

![](https://raw.githubusercontent.com/COMU/zemberek-extension/master/site/images/example.gif)

## Gereksinimler

LibreOffice Türkçe Dil Araçları'nı kullanabilmek için sisteminizde Java 8 veya üzeri bir sürümünün kurulu olması gerekmektedir.

Aşağıdaki komutu kullanarak sisteminizde kurulu Java sürümünü öğrenebilirsiniz.

```
$ java -version
```

Eğer aşağıdaki gibi 1.8.0 ya da üstü bir sürüm olduğunu gösteren bir çıktı alırsanız. Sisteminizdeki Java sürümü LibreOffice Türkçe Dil Araçları'nı kullanmaya uygundur.

```
java version "1.8.0_191"
Java(TM) SE Runtime Environment (build 1.8.0_191-b12)
```

Eğer sisteminizde Java kurulu değilse ya da daha düşük bir Java sürümü kurulu ise işletim sisteminize göre Java 8 kurulumu yapılması gerekir. Örneğin Ubuntu Linux (Ya da Debian, Linux Mint) için terminalden aşağıdaki komutları kullanarak kurulum yapabilirsiniz.

```
$ sudo apt update
$ sudo apt install openjdk-8-jre
```

## Kurulum

* Zemberek LibreOffice eklentisini kurmak için .oxt uzantılı eklenti dosyasını [buradan](https://github.com/COMU/zemberek-extension/releases/download/v0.1.0/libreoffice-tr-tools-0.1.0.oxt) indirin.

* Araçlar (Tools) menüsünden Eklenti Yöneticisini (Extension Manager) seçerek veya **_Ctrl + Alt + E_** kısayolunu kullanarak Eklenti yöneticisini açın.

![Eklenti Yöneticisi](https://raw.githubusercontent.com/COMU/zemberek-extension/master/site/images/extension_manager.png)

* Eklenti yöneticisi penceresinde Ekle (Add) düğmesine tıklayın.

![Eklenti Ekle](https://raw.githubusercontent.com/COMU/zemberek-extension/master/site/images/add_extension.png)

* Açılan dosya seçimi penceresinde daha önce indirdiğiniz eklenti dosyasını seçin ve ardından Aç (Open) düğmesine basın.

![Eklenti Dosyası Seçimi](https://raw.githubusercontent.com/COMU/zemberek-extension/master/site/images/select_extension.png)

* Eklentinin yüklenmesi birkaç saniye alabilir. Eklenti yüklendikten sonra Eklenti yöneticisini kapatabilirsiniz. Eğer uzun bir hata mesajı ekrana gelirse aşağıdaki "Olası Sorunlar" bölümüne bakınız.

![Kapat](https://raw.githubusercontent.com/COMU/zemberek-extension/master/site/images/close_em.png)

* Eklenti yüklendikten sonra kullanmaya başlamak için LibreOffice yeniden başlatılmalıdır. Bunun için Eklenti yöneticisi kapatıldıktan sonra gelen uyarı kutusunda Şimdi Yeniden Başlat (Restart Now) düğmesine tıklayarak LibreOffice'i yeniden başlatabilirsiniz.

![Yeniden Başlat](https://raw.githubusercontent.com/COMU/zemberek-extension/master/site/images/restart.png)

## Olası Sorunlar

* Eklenti, LibreOffice'in başlangıçta bir kaç saniye geç yanıt vermesine sebep olabilir.
* Bu bir erken deneme sürümü olduğu için beklenmedik hatalar ile karşılaşılması, yazım denetimi sırasında bazı yanlışlıkların ortaya çıkması olasıdır. Böyle durumlarda lütfen bizi bilgilendiriniz.
* Eklentinin bellek kullanımı yüksek olabilir (200 Mb'den fazla). Bu, düşük bellekli sistemlerde yavaş çalışma ya da hiç çalışmama sorununa neden olabilir.
* Ubuntu ve Debian Linux'un önceki sürümleri içinde gelen LibreOffice 5 üzerinde eklenti yüklenirken aşağıdakine benzer bir hata mesajı verebilir:

```
(com.sun.star.uno.RuntimeException) { { Message = "[jni_uno bridge error] UNO calling Java method writeRegistryInfo:
non-UNO exception occurred:
java.lang.NoClassDefFoundError: com/sun/star/linguistic2/XSpellChecker\X000ajava
stack trace:\X000a
java.lang.NoClassDefFoundError: com/sun/star/linguistic2/XSpellChecker\X000a\X0009at java.lang.ClassLoader.defineClass1(Native Method)\X000a\X0009at java.lang.ClassLoader.defineClass(ClassLoader.java:763)\X000a\X0009at
```
> Bu durumda terminal ekranından aşağıdaki satırı işletip tekrar deneyebilirsiniz.

```
sudo apt install libreoffice-java-common
```

Bu konularda daha fazla bilgi almak isterseniz [Issues](https://github.com/COMU/zemberek-extension/issues) bölümüne bakabilirsiniz. Yeni bir sorunla karşılaşırsanız yeni bir issue oluşturarak bizi bilgilendirebilirsiniz.

## Lisans

Bu projenin içeriği MPLv2 ile lisanslanmıştır. Detaylı bilgi için [Mozilla Public License 2.0](https://github.com/COMU/zemberek-extension/blob/master/LICENSE) inceleyebilirsiniz.

## Sorumluluk Reddi Beyanı
LibreOffice Türkçe Dil Araçları, belge hazırlama sırasında yapılan yazım ve imla hatalarını en aza indirmek amacıyla hazırlanmış bir Türkçe Yazım Denetimi yazılımıdır. Bu yazılımın kurulumu veya kullanımı sırasında oluşabilecek herhangi bir hasar veya sorundan; kullanıldığı bölgenin yasalarına aykırı şekilde kullanılması durumunda ortaya çıkabilecek herhangi bir yasal işlemden kullanıcının kendisi sorumludur.
