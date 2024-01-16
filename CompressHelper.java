package net.imedicaldoctor.imd.Data;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.Process;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import com.google.android.exoplayer2.metadata.icy.IcyHeaders;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import com.google.common.primitives.UnsignedBytes;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.itextpdf.text.p014io.PagedChannelRandomAccessSource;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.requery.android.database.sqlite.SQLiteDatabase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import net.imedicaldoctor.imd.BuildConfig;
import net.imedicaldoctor.imd.C4804R;
import net.imedicaldoctor.imd.Decompress;
import net.imedicaldoctor.imd.Fragments.AccessMedicine.AMChaptersActivity;
import net.imedicaldoctor.imd.Fragments.Amirsys.ASListActivity;
import net.imedicaldoctor.imd.Fragments.Amirsys.ASListActivityFragment;
import net.imedicaldoctor.imd.Fragments.CMEInfo.CMETOC;
import net.imedicaldoctor.imd.Fragments.CMEInfo.CMETOCFragment;
import net.imedicaldoctor.imd.Fragments.DRE.DREMainActivity;
import net.imedicaldoctor.imd.Fragments.DRE.DREMainActivityFragment;
import net.imedicaldoctor.imd.Fragments.Dictionary.CDicSearchActivity;
import net.imedicaldoctor.imd.Fragments.EPUB.EPUBChaptersActivity;
import net.imedicaldoctor.imd.Fragments.EPUB.EPUBChaptersActivityFragment;
import net.imedicaldoctor.imd.Fragments.Elsevier.ELSChaptersActivity;
import net.imedicaldoctor.imd.Fragments.Epocrate.EPOLabListActivity;
import net.imedicaldoctor.imd.Fragments.Epocrate.EPOLabListActivityFragment;
import net.imedicaldoctor.imd.Fragments.Epocrate.EPOMainActivity;
import net.imedicaldoctor.imd.Fragments.Epocrate.EPOMainActivityFragment;
import net.imedicaldoctor.imd.Fragments.Facts.FTListActivity;
import net.imedicaldoctor.imd.Fragments.Facts.FTListActivityFragment;
import net.imedicaldoctor.imd.Fragments.IranDaru.IDSearchActivity;
import net.imedicaldoctor.imd.Fragments.IranGenericDrugs.IranGenericDrugsList;
import net.imedicaldoctor.imd.Fragments.IranGenericDrugs.IranGenericDrugsListFragment;
import net.imedicaldoctor.imd.Fragments.LWW.LWWChapters;
import net.imedicaldoctor.imd.Fragments.LWW.LWWChaptersFragment;
import net.imedicaldoctor.imd.Fragments.Lexi.LXItems;
import net.imedicaldoctor.imd.Fragments.LexiInteract.LXInteractList;
import net.imedicaldoctor.imd.Fragments.LexiInteract.LXIvInteract;
import net.imedicaldoctor.imd.Fragments.Martindale.MDListActivity;
import net.imedicaldoctor.imd.Fragments.Martindale.MDListActivityFragment;
import net.imedicaldoctor.imd.Fragments.Medhand.MHSearchActivity;
import net.imedicaldoctor.imd.Fragments.Micromedex.MMIVSelectActivity;
import net.imedicaldoctor.imd.Fragments.Micromedex.MMIVSelectActivityFragment;
import net.imedicaldoctor.imd.Fragments.Micromedex.MMInteractSelectActivity;
import net.imedicaldoctor.imd.Fragments.Micromedex.MMInteractSelectActivityFragment;
import net.imedicaldoctor.imd.Fragments.Micromedex.MMListActivity;
import net.imedicaldoctor.imd.Fragments.Micromedex.MMListActivityFragment;
import net.imedicaldoctor.imd.Fragments.Micromedex.MMNeoListActivity;
import net.imedicaldoctor.imd.Fragments.Micromedex.MMNeoListActivityFragment;
import net.imedicaldoctor.imd.Fragments.NEJM.NEJMTOCActivity;
import net.imedicaldoctor.imd.Fragments.Noskheha.NOSListActivity;
import net.imedicaldoctor.imd.Fragments.Noskheha.NOSListActivityFragment;
import net.imedicaldoctor.imd.Fragments.OVID.OvidChaptersActivity;
import net.imedicaldoctor.imd.Fragments.Sanford.SANTocActivity;
import net.imedicaldoctor.imd.Fragments.Sanford.SANTocActivityFragment;
import net.imedicaldoctor.imd.Fragments.Skyscape.SSSearchActivity;
import net.imedicaldoctor.imd.Fragments.Statdx.SDListActivity;
import net.imedicaldoctor.imd.Fragments.Statdx.SDListActivityFragment;
import net.imedicaldoctor.imd.Fragments.Stockley.STListActivity;
import net.imedicaldoctor.imd.Fragments.Stockley.STListActivityFragment;
import net.imedicaldoctor.imd.Fragments.TOL.PsychoListActivity;
import net.imedicaldoctor.imd.Fragments.TOL.PsychoListActivityFragment;
import net.imedicaldoctor.imd.Fragments.UTDAdvanced.UTDASearchActivity;
import net.imedicaldoctor.imd.Fragments.UTDAdvanced.UTDASearchActivityFragment;
import net.imedicaldoctor.imd.Fragments.UWorld.UWMainActivity;
import net.imedicaldoctor.imd.Fragments.UWorld.UWMainActivityFragment;
import net.imedicaldoctor.imd.Fragments.Uptodate.UTDSearchActivity;
import net.imedicaldoctor.imd.Fragments.UptodateDDX.UTDDSearchActivity;
import net.imedicaldoctor.imd.Fragments.VisualDDX.VDDxScenarioActivity;
import net.imedicaldoctor.imd.Fragments.VisualDXLookup.VDSearchActivity;
import net.imedicaldoctor.imd.Fragments.mainActivity;
import net.imedicaldoctor.imd.VBHelper;
import net.imedicaldoctor.imd.iMD;
import net.imedicaldoctor.imd.iMDLogger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.p024io.FileUtils;
import org.apache.commons.p024io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CompressHelper {

    /* renamed from: g */
    public static final String f73766g = "fileSize";

    /* renamed from: h */
    public static final String f73767h = "MD5";

    /* renamed from: i */
    public static final String f73768i = "bytesDownloaded";

    /* renamed from: j */
    public static final String f73769j = "bytesTotal";

    /* renamed from: k */
    public static final String f73770k = "avgSpeed";

    /* renamed from: l */
    public static final String f73771l = "remaining";

    /* renamed from: m */
    public static final String f73772m = "downloader";

    /* renamed from: n */
    public static MediaPlayer f73773n = null;

    /* renamed from: o */
    public static ArrayList<String> f73774o = null;

    /* renamed from: p */
    public static final String f73775p = ",visualdx.png,uptodate.png,irandarou.png,";

    /* renamed from: q */
    public static Fragment f73776q = null;

    /* renamed from: r */
    public static HashMap<String, Stack<Fragment>> f73777r = null;

    /* renamed from: s */
    public static ArrayList<Bundle> f73778s = null;

    /* renamed from: t */
    private static final Logger f73779t = Logger.getLogger(CompressHelper.class.toString());

    /* renamed from: u */
    public static final String f73780u = "master";

    /* renamed from: v */
    public static final String f73781v = "detail";

    /* renamed from: w */
    public static boolean f73782w;

    /* renamed from: a */
    public Bundle f73783a;

    /* renamed from: b */
    public ArrayList<File> f73784b;

    /* renamed from: c */
    public String f73785c;

    /* renamed from: d */
    public Context f73786d;

    /* renamed from: e */
    public Bundle f73787e;

    /* renamed from: f */
    public VBHelper f73788f;

    /* renamed from: net.imedicaldoctor.imd.Data.CompressHelper$10 */
    /* loaded from: classes2.dex */
    class C364810 extends DisposableObserver<String> {
        C364810() {
        }

        @Override // io.reactivex.rxjava3.core.Observer
        /* renamed from: b */
        public void onNext(@NonNull String str) {
            StringUtils.splitByWholeSeparator(str, "|||||");
        }

        @Override // io.reactivex.rxjava3.core.Observer
        public void onComplete() {
        }

        @Override // io.reactivex.rxjava3.core.Observer
        public void onError(@NonNull Throwable th) {
        }
    }

    /* renamed from: net.imedicaldoctor.imd.Data.CompressHelper$11 */
    /* loaded from: classes2.dex */
    class C364911 extends DisposableObserver<String> {
        C364911() {
        }

        @Override // io.reactivex.rxjava3.core.Observer
        /* renamed from: b */
        public void onNext(String str) {
            StringUtils.splitByWholeSeparator(str, "|||||");
        }

        @Override // io.reactivex.rxjava3.core.Observer
        public void onComplete() {
        }

        @Override // io.reactivex.rxjava3.core.Observer
        public void onError(Throwable th) {
        }
    }

    public CompressHelper(Context context) {
        if (f73777r == null) {
            HashMap<String, Stack<Fragment>> hashMap = new HashMap<>();
            f73777r = hashMap;
            hashMap.put(f73780u, new Stack<>());
            f73777r.put(f73781v, new Stack<>());
        }
        this.f73786d = context;
        this.f73788f = new VBHelper(context);
        this.f73783a = new Bundle();
    }

    /* renamed from: C */
    public static String m5012C(Bundle bundle) {
        try {
            String string = bundle.getString("IconName");
            if (f73775p.contains("," + string + ",")) {
                return "file:///android_asset/" + string;
            }
            return bundle.getString("Path") + "/" + string;
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            return "file:///android_asset/placeholder.png";
        }
    }

    /* renamed from: D */
    public static String m5009D(Bundle bundle) {
        String[] splitByWholeSeparator = StringUtils.splitByWholeSeparator(bundle.getString("dbIcon"), "/");
        String str = splitByWholeSeparator[splitByWholeSeparator.length - 1];
        if (f73775p.contains("," + str + ",")) {
            return "file:///android_asset/" + str;
        }
        return bundle.getString("dbIcon");
    }

    /* renamed from: E0 */
    public static byte[] m5005E0(byte[] bArr) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        byte[] bArr2 = new byte[1024];
        while (!inflater.finished()) {
            byteArrayOutputStream.write(bArr2, 0, inflater.inflate(bArr2));
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /* renamed from: F1 */
    public static void m5001F1(Context context, String str) {
        try {
            MediaPlayer mediaPlayer = f73773n;
            if (mediaPlayer != null) {
                mediaPlayer.release();
                f73773n = null;
            }
            if (f73773n == null) {
                f73773n = new MediaPlayer();
            }
            if (f73773n.isPlaying()) {
                f73773n.stop();
            }
            AssetFileDescriptor openFd = context.getAssets().openFd(str);
            f73773n.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            openFd.close();
            f73773n.prepare();
            f73773n.setVolume(1.0f, 1.0f);
            f73773n.setLooping(false);
            f73773n.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: H0 */
    public static void m4996H0(Context context, String str, String str2, String str3) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.mo26311b(false);
            builder.mo26266y("اطلاعات بیشتر", new DialogInterface.OnClickListener() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.mo26284p("باشه", new DialogInterface.OnClickListener() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.7
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.setTitle(str);
            builder.mo26292l(str2);
            AlertDialog create = builder.create();
            ((TextView) create.findViewById(16908299)).setTypeface(ResourcesCompat.m47479g(context, C4804R.font.f86758iransans));
            create.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: R1 */
    public static String m4965R1(String str) {
        ArrayList arrayList = new ArrayList(Arrays.asList(StringUtils.splitByWholeSeparator(str, "/")));
        arrayList.remove(arrayList.size() - 1);
        return StringUtils.join(arrayList, "/");
    }

    /* renamed from: U0 */
    public static String m4957U0(String str) {
        try {
            return (str.length() == 6 ? new SimpleDateFormat("MMM, yyyy") : new SimpleDateFormat("MMM d, yyyy")).format((str.length() == 6 ? new SimpleDateFormat("yyyyMM") : new SimpleDateFormat("yyyyMMdd")).parse(str));
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            return str;
        }
    }

    /* renamed from: V0 */
    public static byte[] m4954V0(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gZIPOutputStream.write(bArr);
        gZIPOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return byteArray;
    }

    /* renamed from: W0 */
    public static byte[] m4951W0(byte[] bArr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream, 32);
        byte[] bArr2 = new byte[32];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        while (true) {
            int read = gZIPInputStream.read(bArr2);
            if (read == -1) {
                gZIPInputStream.close();
                byteArrayInputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr2, 0, read);
        }
    }

    /* renamed from: X0 */
    public static String m4948X0(Bundle bundle) {
        return bundle.getString("Path") + "/" + bundle.getString("Name");
    }

    /* renamed from: Y0 */
    public static String m4945Y0(Bundle bundle, String str) {
        return bundle.getString("Path") + "/" + str;
    }

    /* renamed from: Z0 */
    public static String m4942Z0(Bundle bundle, String str, String str2) {
        return bundle.getString("Path") + "/" + str2 + "/" + str;
    }

    /* renamed from: b */
    public static int m4936b(ArrayList<Bundle> arrayList, Bundle bundle, String str) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getString(str).equals(bundle.getString(str))) {
                return i;
            }
        }
        return -1;
    }

    /* renamed from: c1 */
    public static HashSet<String> m4930c1() {
        String[] split;
        String[] split2;
        byte[] bArr;
        HashSet<String> hashSet = new HashSet<>();
        String str = "";
        try {
            Process start = new ProcessBuilder(new String[0]).command("mount").redirectErrorStream(true).start();
            start.waitFor();
            InputStream inputStream = start.getInputStream();
            while (inputStream.read(new byte[1024]) != -1) {
                str = str + new String(bArr);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String str2 : str.split("\n")) {
            if (!str2.toLowerCase(Locale.US).contains("asec") && str2.matches("(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*")) {
                for (String str3 : str2.split(StringUtils.SPACE)) {
                    if (str3.startsWith("/") && !str3.toLowerCase(Locale.US).contains("vold")) {
                        hashSet.add(str3);
                    }
                }
            }
        }
        return hashSet;
    }

    /* renamed from: e2 */
    public static void m4921e2(Context context, String str, int i) {
        if (context == null) {
            return;
        }
        try {
            Toast.makeText(context, str, i).show();
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("Error", "Error in showtoast");
            e.printStackTrace();
        }
    }

    /* renamed from: f */
    public static String m4920f(String str, String str2, String str3) {
        if (str.contains(str2) && str.contains(str3)) {
            String[] splitByWholeSeparator = StringUtils.splitByWholeSeparator(str, str2);
            return StringUtils.splitByWholeSeparator(splitByWholeSeparator[splitByWholeSeparator.length - 1], str3)[0];
        }
        return null;
    }

    /* renamed from: g1 */
    public static Bundle m4914g1(ArrayList<Bundle> arrayList, final String str, final String str2) {
        ArrayList arrayList2 = new ArrayList(Collections2.m23110e(arrayList, new Predicate<Bundle>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.14
            @Override // com.google.common.base.Predicate
            /* renamed from: a */
            public boolean apply(Bundle bundle) {
                try {
                    return bundle.getString(str).equals(str2);
                } catch (Exception e) {
                    FirebaseCrashlytics.m18030d().m18027g(e);
                    iMDLogger.m3294f("Error in filtering", e.getLocalizedMessage());
                    return false;
                }
            }
        }));
        if (arrayList2.size() == 0) {
            return null;
        }
        return (Bundle) arrayList2.get(0);
    }

    /* renamed from: k1 */
    public static String m4901k1(Bundle bundle, String str) {
        return bundle.getString("Path") + "/" + str;
    }

    /* renamed from: l1 */
    public static String m4898l1() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int nextInt = random.nextInt(20);
        for (int i = 0; i < nextInt; i++) {
            sb.append((char) (random.nextInt(96) + 32));
        }
        return sb.toString();
    }

    /* renamed from: t1 */
    public static String m4874t1(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & UnsignedBytes.f54281b);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: v1 */
    public static String m4868v1(ArrayList<Bundle> arrayList, String str) {
        return m4865w1(arrayList, str, ",", "", "");
    }

    /* renamed from: w1 */
    public static String m4865w1(ArrayList<Bundle> arrayList, String str, String str2, String str3, String str4) {
        ArrayList arrayList2 = new ArrayList();
        Iterator<Bundle> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            arrayList2.add(str3 + it2.next().getString(str) + str4);
        }
        return TextUtils.join(str2, arrayList2);
    }

    /* renamed from: x1 */
    public static String m4862x1(String str) {
        ArrayList arrayList = new ArrayList(Arrays.asList(StringUtils.splitByWholeSeparator(str, "/")));
        return (String) arrayList.get(arrayList.size() - 1);
    }

    /* renamed from: z0 */
    public static byte[] m4857z0(byte[] bArr) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(-1);
        deflater.setInput(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length);
        deflater.finish();
        byte[] bArr2 = new byte[5120];
        while (!deflater.finished()) {
            byteArrayOutputStream.write(bArr2, 0, deflater.deflate(bArr2));
        }
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /* renamed from: A */
    public String m5018A() {
        String str = "";
        try {
            str = m5004E1() + "/spell.db";
            if (!new File(str).exists()) {
                InputStream open = this.f73786d.getAssets().open("spell.db");
                FileOutputStream fileOutputStream = new FileOutputStream(str);
                byte[] bArr = new byte[1048576];
                while (true) {
                    int read = open.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                open.close();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f(CompressHelper.class.toString(), e.getMessage());
        }
        return str;
    }

    /* renamed from: A0 */
    public Observable<String> m5017A0(Activity activity, Observable<String> observable) {
        return observable.m7300i6(Schedulers.m5370e()).m7193t4(AndroidSchedulers.m8490e());
    }

    /* renamed from: A1 */
    public boolean m5016A1(Bundle bundle, String str) {
        String decode = URLDecoder.decode(str);
        if (decode.contains("rx/monograph/")) {
            String valueOf = String.valueOf(Integer.valueOf(m4904j1(StringUtils.splitByWholeSeparator(decode, "rx/monograph/"))).intValue() + 1);
            m4883q1(bundle, "rx-" + valueOf, null, null);
            return true;
        } else if (decode.contains("dx/monograph/")) {
            String m4904j1 = m4904j1(StringUtils.splitByWholeSeparator(decode, "dx/monograph/"));
            m4883q1(bundle, "dx-" + m4904j1, null, null);
            return true;
        } else if (decode.contains("lab/monograph/")) {
            String m4904j12 = m4904j1(StringUtils.splitByWholeSeparator(decode, "lab/monograph/"));
            m4883q1(bundle, "lab-" + m4904j12, null, null);
            return true;
        } else if (decode.contains("lab/list/panel/")) {
            String m4904j13 = m4904j1(StringUtils.splitByWholeSeparator(decode, "lab/list/panel/"));
            Bundle m4907i1 = m4907i1(m4955V(bundle, "Select * from lab_panel where id2=" + m4904j13));
            if (m4907i1 != null) {
                Bundle bundle2 = new Bundle();
                bundle2.putBundle("DB", bundle);
                bundle2.putString("ParentId", m4907i1.getString("id"));
                m4979N(EPOLabListActivity.class, EPOLabListActivityFragment.class, bundle2);
            }
            return true;
        } else if (!decode.contains("rx/list/drug?select=")) {
            iMDLogger.m3290j("manageEpocrateURL", "Can't manage " + decode);
            return false;
        } else {
            String replace = m4904j1(StringUtils.splitByWholeSeparator(decode, "rx/list/drug?select=")).replace("*", "");
            Bundle m4907i12 = m4907i1(m4952W(bundle, "select * from drug where name='" + replace + "'", "RX.sqlite"));
            if (m4907i12 == null) {
                m4921e2(this.f73786d, "Sorry, Can't find it", 1);
                return true;
            }
            m4883q1(bundle, "rx-" + m4907i12.getString("ID"), null, null);
            return true;
        }
    }

    /* renamed from: B */
    public String m5015B(String str, String str2, String str3) {
        String str4;
        if (!str3.equals("127")) {
            return null;
        }
        VBHelper vBHelper = this.f73788f;
        String str5 = TextUtils.split(vBHelper.m3410w(vBHelper.m3421l()).replace("||", "::"), "::")[1];
        byte[] decode = Base64.decode(str, 0);
        for (int length = str2.length(); length < 8; length++) {
            str2 = str2 + StringUtils.SPACE;
        }
        try {
            try {
                return new String(m4951W0(m5002F0(str5.toCharArray(), str2.getBytes("UTF-8"), new byte[]{17, 115, 105, 102, 103, 104, 111, 107, 108, 122, 120, 119, 118, 98, 110, 109}, decode)));
            } catch (Exception e) {
                e = e;
                str4 = "CompressHelper _ GetString Decompressing";
                iMDLogger.m3294f(str4, e.toString());
                return null;
            }
        } catch (Exception e2) {
            e = e2;
            str4 = "CompressHelper _ GetString Decryption";
        }
    }

    /* renamed from: B0 */
    public Observable<String> m5014B0(Fragment fragment, Observable<String> observable) {
        return observable.m7300i6(Schedulers.m5370e()).m7193t4(AndroidSchedulers.m8490e());
    }

    /* renamed from: B1 */
    public String m5013B1(File file) {
        try {
            MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            String encodeToString = Base64.encodeToString(DigestUtils.md5(fileInputStream), 0);
            fileInputStream.close();
            return encodeToString;
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            return "";
        }
    }

    /* renamed from: C0 */
    public byte[] m5011C0(char[] cArr, byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(cArr, bArr, 19, 128)).getEncoded(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(1, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(bArr3);
    }

    /* renamed from: C1 */
    public String m5010C1(String str) {
        return str;
    }

    /* renamed from: D0 */
    public ArrayList<String> m5008D0() {
        ArrayList<Bundle> arrayList = ((iMD) this.f73786d.getApplicationContext()).f83461s;
        ArrayList<String> arrayList2 = new ArrayList<>();
        if ((arrayList.size() == 0) || (arrayList == null)) {
            return arrayList2;
        }
        Iterator<Bundle> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            arrayList2.add(it2.next().getString("Name"));
        }
        return arrayList2;
    }

    /* renamed from: D1 */
    public String m5007D1(String str) {
        return str;
    }

    /* renamed from: E */
    public ArrayList<Object> m5006E(JSONArray jSONArray) {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < jSONArray.length(); i++) {
            new Bundle();
            try {
                if (!jSONArray.isNull(i)) {
                    Object obj = jSONArray.get(i);
                    if (obj.getClass() != JSONArray.class) {
                        arrayList.add(obj.getClass() == JSONObject.class ? m5000G((JSONObject) obj) : jSONArray.getString(i));
                    }
                }
            } catch (Exception e) {
                FirebaseCrashlytics.m18030d().m18027g(e);
                e.getStackTrace()[0].getLineNumber();
                ArrayList arrayList2 = new ArrayList();
                for (int i2 = 0; i2 < e.getStackTrace().length; i2++) {
                    String str = e.getStackTrace()[i2].getClassName() + " - " + e.getStackTrace()[i2].getLineNumber();
                    arrayList2.add(str);
                    iMDLogger.m3294f("JSONArrayTo", str);
                }
                e.printStackTrace();
                iMDLogger.m3294f("JSONArrayToBundle", "Error in parsing");
                return null;
            }
        }
        return arrayList;
    }

    /* renamed from: E1 */
    public String m5004E1() {
        return this.f73786d.getExternalFilesDir("").toString();
    }

    /* renamed from: F */
    public ArrayList<Bundle> m5003F(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            new Bundle();
            try {
                Object obj = jSONArray.get(i);
                if (obj.getClass() != JSONArray.class) {
                    arrayList.add(obj.getClass() == JSONObject.class ? m5000G((JSONObject) obj) : jSONArray.getString(i));
                }
            } catch (Exception e) {
                FirebaseCrashlytics.m18030d().m18027g(e);
                iMDLogger.m3294f("JSONArrayToBundle", "Error in parsing");
                return null;
            }
        }
        ArrayList<Bundle> arrayList2 = new ArrayList<>();
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            arrayList2.add((Bundle) it2.next());
        }
        return arrayList2;
    }

    /* renamed from: F0 */
    public byte[] m5002F0(char[] cArr, byte[] bArr, byte[] bArr2, byte[] bArr3) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(cArr, bArr, 19, 128)).getEncoded(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(2, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(bArr3);
    }

    /* renamed from: G */
    public Bundle m5000G(JSONObject jSONObject) throws Exception {
        Bundle bundle = new Bundle();
        Iterator it2 = Lists.m21958s(jSONObject.keys()).iterator();
        while (it2.hasNext()) {
            String str = (String) it2.next();
            Object obj = jSONObject.get(str);
            if (obj.getClass() == JSONArray.class) {
                ArrayList<Object> m5006E = m5006E((JSONArray) obj);
                if (m5006E.size() == 0) {
                    bundle.putParcelableArrayList(str, new ArrayList<>());
                } else if (m5006E.get(0).getClass() == Bundle.class) {
                    ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                    Iterator<Object> it3 = m5006E.iterator();
                    while (it3.hasNext()) {
                        arrayList.add((Bundle) it3.next());
                    }
                    bundle.putParcelableArrayList(str, arrayList);
                } else {
                    ArrayList<String> arrayList2 = new ArrayList<>();
                    Iterator<Object> it4 = m5006E.iterator();
                    while (it4.hasNext()) {
                        arrayList2.add((String) it4.next());
                    }
                    bundle.putStringArrayList(str, arrayList2);
                }
            } else if (obj.getClass() == JSONObject.class) {
                bundle.putBundle(str, m5000G((JSONObject) obj));
            } else {
                bundle.putString(str, jSONObject.getString(str));
            }
        }
        return bundle;
    }

    /* renamed from: G0 */
    public void m4999G0(char[] cArr, byte[] bArr, byte[] bArr2, FileInputStream fileInputStream, FileOutputStream fileOutputStream) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(cArr, bArr, 19, 128)).getEncoded(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(2, secretKeySpec, ivParameterSpec);
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);
        IOUtils.copyLarge(cipherInputStream, fileOutputStream);
        cipherInputStream.close();
        fileOutputStream.close();
    }

    /* renamed from: G1 */
    public boolean m4998G1(boolean z) {
        String str;
        int i;
        if (!m4892n1()) {
            ((Activity) this.f73786d).finish();
            ((Activity) this.f73786d).overridePendingTransition(C4804R.anim.f85978to_fade_in, C4804R.anim.f85979to_fade_out);
            return false;
        }
        if (z) {
            str = f73780u;
            i = C4804R.C4808id.f87010rootcontainer;
        } else {
            str = f73781v;
            i = C4804R.C4808id.f86865detail_container;
        }
        return m4995H1(str, i);
    }

    /* renamed from: H */
    public void m4997H(String str, String str2, String str3) {
    }

    /* renamed from: H1 */
    public boolean m4995H1(String str, int i) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (f73777r.get(str).size() == 0) {
            return false;
        }
        if (f73777r.get(str).size() > 1) {
            Fragment elementAt = f73777r.get(str).elementAt(f73777r.get(str).size() - 2);
            f73777r.get(str).pop();
            FragmentTransaction m44464r = ((AppCompatActivity) this.f73786d).m44690E().m44464r();
            m44464r.m44309I(C4804R.anim.f85978to_fade_in, C4804R.anim.f85979to_fade_out);
            if (m4892n1() && str.equals(f73781v)) {
                m44464r.m44309I(C4804R.anim.f85969fade_out, C4804R.anim.f85968fade_in);
            }
            m44464r.m44278y(i, elementAt);
            m44464r.mo44289n();
        } else {
            Fragment elementAt2 = f73777r.get(str).elementAt(f73777r.get(str).size() - 1);
            f73777r.get(str).pop();
            FragmentTransaction m44464r2 = ((AppCompatActivity) this.f73786d).m44690E().m44464r();
            m44464r2.m44309I(C4804R.anim.f85978to_fade_in, C4804R.anim.f85979to_fade_out);
            if (m4892n1() && str.equals(f73781v)) {
                m44464r2.m44309I(C4804R.anim.f85969fade_out, C4804R.anim.f85968fade_in);
            }
            m44464r2.mo44279x(elementAt2);
            m44464r2.mo44289n();
        }
        return true;
    }

    /* renamed from: I */
    public void m4994I(String str, String str2) {
    }

    /* renamed from: I0 */
    public void m4993I0(final Runnable runnable, final Runnable runnable2) {
        Observable.m7156x1(new ObservableOnSubscribe<String>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.27
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<String> observableEmitter) throws Throwable {
                try {
                    runnable.run();
                    observableEmitter.onNext("asdfadf");
                } catch (Exception unused) {
                }
            }
        }).m7300i6(Schedulers.m5370e()).m7193t4(AndroidSchedulers.m8490e()).m7329f6(new Consumer<String>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.28
            @Override // io.reactivex.rxjava3.functions.Consumer
            /* renamed from: a */
            public void accept(String str) throws Throwable {
                try {
                    runnable2.run();
                } catch (Exception e) {
                    FirebaseCrashlytics.m18030d().m18027g(e);
                }
            }
        }, new Consumer<Throwable>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.29
            @Override // io.reactivex.rxjava3.functions.Consumer
            /* renamed from: a */
            public void accept(Throwable th) throws Throwable {
                try {
                    iMDLogger.m3294f("Error occured", th.getMessage());
                } catch (Exception e) {
                    FirebaseCrashlytics.m18030d().m18027g(e);
                }
            }
        });
    }

    /* renamed from: I1 */
    public void m4992I1(String str, int i) {
        try {
            if (f73777r.get(str).size() > 1) {
                while (f73777r.get(str).size() == 1) {
                    f73777r.get(str).pop();
                }
            }
            Fragment elementAt = f73777r.get(str).elementAt(f73777r.get(str).size() - 1);
            f73777r.get(str).pop();
            FragmentTransaction m44464r = ((AppCompatActivity) this.f73786d).m44690E().m44464r();
            m44464r.m44309I(C4804R.anim.f85978to_fade_in, C4804R.anim.f85979to_fade_out);
            if (m4892n1() && str.equals(f73781v)) {
                m44464r.m44309I(C4804R.anim.f85969fade_out, C4804R.anim.f85968fade_in);
            }
            m44464r.mo44279x(elementAt);
            m44464r.mo44289n();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: J */
    public String m4991J() {
        String str = PreferenceManager.getDefaultSharedPreferences(this.f73786d).getString("MainServer", "Iran").equals("Iran") ? "si.imedicaldoctor.net" : "sg.imedicaldoctor.net";
        return "http://" + str;
    }

    /* renamed from: J0 */
    public void m4990J0(String str, String str2) throws IOException {
        PrintStream printStream;
        String str3;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(180000);
        int responseCode = httpURLConnection.getResponseCode();
        if (new File(str2).exists()) {
            new File(str2).delete();
        }
        if (responseCode == 200) {
            String headerField = httpURLConnection.getHeaderField(HttpHeaders.f53950W);
            String contentType = httpURLConnection.getContentType();
            int contentLength = httpURLConnection.getContentLength();
            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + headerField);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("filePath = " + str2);
            InputStream inputStream = httpURLConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[102400];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.close();
            inputStream.close();
            printStream = System.out;
            str3 = "File downloaded";
        } else {
            printStream = System.out;
            str3 = "No file to download. Server replied HTTP code: " + responseCode;
        }
        printStream.println(str3);
        httpURLConnection.disconnect();
    }

    /* renamed from: J1 */
    public void m4989J1(boolean z) {
        String str;
        int i;
        if (!m4892n1()) {
            Intent intent = new Intent(this.f73786d, mainActivity.class);
            intent.addFlags(PagedChannelRandomAccessSource.f60487g);
            ((Activity) this.f73786d).startActivity(intent);
            ((Activity) this.f73786d).overridePendingTransition(C4804R.anim.f85978to_fade_in, C4804R.anim.f85979to_fade_out);
            return;
        }
        if (z) {
            str = f73780u;
            i = C4804R.C4808id.f87010rootcontainer;
        } else {
            str = f73781v;
            i = C4804R.C4808id.f86865detail_container;
        }
        m4992I1(str, i);
    }

    /* renamed from: K */
    public String m4988K() {
        String absolutePath = Environment.getExternalStoragePublicDirectory("Documents").getAbsolutePath();
        new File(absolutePath).mkdirs();
        return absolutePath + "/.iMD";
    }

    /* renamed from: K0 */
    public Observable<String> m4987K0(final String str, final String str2) {
        return Observable.m7156x1(new ObservableOnSubscribe<String>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.2
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<String> observableEmitter) throws Throwable {
                try {
                    CompressHelper.this.m4990J0(str, str2);
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    iMDLogger.m3294f("DownloadFile", "Error in downloading file " + e.toString());
                    observableEmitter.onError(e);
                }
            }
        });
    }

    /* renamed from: K1 */
    public void m4986K1(String str, Fragment fragment, int i, boolean z, boolean z2) {
        if (z2) {
            try {
                f73777r.get(str).push(fragment);
            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrashlytics.m18030d().m18027g(e);
                return;
            }
        }
        if (str.equals(f73781v) && f73777r.get(str).size() > 3) {
            f73777r.get(str).removeElementAt(0);
        }
        FragmentTransaction m44464r = ((AppCompatActivity) this.f73786d).m44690E().m44464r();
        if (z) {
            m44464r.m44309I(C4804R.anim.f85970from_fade_in, C4804R.anim.f85971from_fade_out);
        }
        if (m4892n1() && str.equals(f73781v)) {
            m44464r.m44309I(C4804R.anim.f85968fade_in, C4804R.anim.f85969fade_out);
        }
        m44464r.m44278y(i, fragment);
        m44464r.mo44289n();
    }

    /* renamed from: L */
    public String m4985L() {
        String absolutePath = Environment.getExternalStoragePublicDirectory("Documents").getAbsolutePath();
        new File(absolutePath).mkdirs();
        return absolutePath + "/iMD";
    }

    /* renamed from: L0 */
    public Observable<HttpURLConnection> m4984L0(final String str, final String str2, final Bundle bundle) {
        final String str3 = str2 + ".download";
        final String str4 = str2 + ".md5";
        return Observable.m7156x1(new ObservableOnSubscribe<HttpURLConnection>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.3
            /* JADX WARN: Code restructure failed: missing block: B:109:0x03ef, code lost:
                r9.close();
                r5.disconnect();
                r25.onError(new java.lang.Throwable("Stopped"));
             */
            /* JADX WARN: Code restructure failed: missing block: B:110:0x03ff, code lost:
                return;
             */
            /* JADX WARN: Removed duplicated region for block: B:103:0x03cf A[Catch: Exception -> 0x054c, TryCatch #0 {Exception -> 0x054c, blocks: (B:3:0x0008, B:6:0x0027, B:8:0x004f, B:10:0x005b, B:11:0x0065, B:17:0x007b, B:19:0x00c6, B:21:0x00e8, B:23:0x00f5, B:24:0x00fd, B:26:0x0103, B:29:0x010a, B:31:0x0143, B:33:0x0147, B:35:0x0159, B:37:0x0165, B:38:0x016f, B:43:0x0186, B:45:0x0197, B:47:0x01a3, B:49:0x01b2, B:52:0x01bf, B:54:0x01c5, B:56:0x01e0, B:58:0x01f8, B:59:0x0217, B:66:0x0251, B:68:0x026e, B:70:0x02be, B:75:0x02e2, B:77:0x031d, B:79:0x032f, B:81:0x033c, B:84:0x034c, B:86:0x0358, B:87:0x035e, B:88:0x0363, B:98:0x0391, B:100:0x03b8, B:101:0x03c8, B:103:0x03cf, B:105:0x03d9, B:108:0x03ea, B:109:0x03ef, B:111:0x0400, B:113:0x0415, B:115:0x0437, B:117:0x0444, B:118:0x044c, B:120:0x0452, B:122:0x0469, B:124:0x0483, B:126:0x04c2, B:130:0x04dd, B:132:0x0506, B:91:0x0374, B:93:0x0380, B:60:0x021b, B:62:0x0230, B:64:0x0243), top: B:137:0x0008 }] */
            /* JADX WARN: Removed duplicated region for block: B:140:0x0400 A[SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:54:0x01c5 A[Catch: Exception -> 0x054c, TryCatch #0 {Exception -> 0x054c, blocks: (B:3:0x0008, B:6:0x0027, B:8:0x004f, B:10:0x005b, B:11:0x0065, B:17:0x007b, B:19:0x00c6, B:21:0x00e8, B:23:0x00f5, B:24:0x00fd, B:26:0x0103, B:29:0x010a, B:31:0x0143, B:33:0x0147, B:35:0x0159, B:37:0x0165, B:38:0x016f, B:43:0x0186, B:45:0x0197, B:47:0x01a3, B:49:0x01b2, B:52:0x01bf, B:54:0x01c5, B:56:0x01e0, B:58:0x01f8, B:59:0x0217, B:66:0x0251, B:68:0x026e, B:70:0x02be, B:75:0x02e2, B:77:0x031d, B:79:0x032f, B:81:0x033c, B:84:0x034c, B:86:0x0358, B:87:0x035e, B:88:0x0363, B:98:0x0391, B:100:0x03b8, B:101:0x03c8, B:103:0x03cf, B:105:0x03d9, B:108:0x03ea, B:109:0x03ef, B:111:0x0400, B:113:0x0415, B:115:0x0437, B:117:0x0444, B:118:0x044c, B:120:0x0452, B:122:0x0469, B:124:0x0483, B:126:0x04c2, B:130:0x04dd, B:132:0x0506, B:91:0x0374, B:93:0x0380, B:60:0x021b, B:62:0x0230, B:64:0x0243), top: B:137:0x0008 }] */
            /* JADX WARN: Removed duplicated region for block: B:60:0x021b A[Catch: Exception -> 0x054c, TryCatch #0 {Exception -> 0x054c, blocks: (B:3:0x0008, B:6:0x0027, B:8:0x004f, B:10:0x005b, B:11:0x0065, B:17:0x007b, B:19:0x00c6, B:21:0x00e8, B:23:0x00f5, B:24:0x00fd, B:26:0x0103, B:29:0x010a, B:31:0x0143, B:33:0x0147, B:35:0x0159, B:37:0x0165, B:38:0x016f, B:43:0x0186, B:45:0x0197, B:47:0x01a3, B:49:0x01b2, B:52:0x01bf, B:54:0x01c5, B:56:0x01e0, B:58:0x01f8, B:59:0x0217, B:66:0x0251, B:68:0x026e, B:70:0x02be, B:75:0x02e2, B:77:0x031d, B:79:0x032f, B:81:0x033c, B:84:0x034c, B:86:0x0358, B:87:0x035e, B:88:0x0363, B:98:0x0391, B:100:0x03b8, B:101:0x03c8, B:103:0x03cf, B:105:0x03d9, B:108:0x03ea, B:109:0x03ef, B:111:0x0400, B:113:0x0415, B:115:0x0437, B:117:0x0444, B:118:0x044c, B:120:0x0452, B:122:0x0469, B:124:0x0483, B:126:0x04c2, B:130:0x04dd, B:132:0x0506, B:91:0x0374, B:93:0x0380, B:60:0x021b, B:62:0x0230, B:64:0x0243), top: B:137:0x0008 }] */
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void mo3518a(@io.reactivex.rxjava3.annotations.NonNull io.reactivex.rxjava3.core.ObservableEmitter<java.net.HttpURLConnection> r25) throws java.lang.Throwable {
                /*
                    Method dump skipped, instructions count: 1398
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: net.imedicaldoctor.imd.Data.CompressHelper.C36693.mo3518a(io.reactivex.rxjava3.core.ObservableEmitter):void");
            }
        });
    }

    /* renamed from: L1 */
    public long m4983L1() {
        long longValue;
        Bundle m4907i1 = m4907i1(m4946Y(m4971P1(), "Select c from r where id=2"));
        Date date = new Date();
        if (m4907i1 == null) {
            String m4971P1 = m4971P1();
            m4885q(m4971P1, "insert into r values (2," + date.getTime() + ")");
            longValue = date.getTime();
        } else {
            longValue = Long.valueOf(m4907i1.getString("c")).longValue();
        }
        String m4971P12 = m4971P1();
        m4885q(m4971P12, "update r set c=" + date.getTime() + " where id=2");
        return longValue;
    }

    /* renamed from: M */
    public void m4982M(Class<?> cls, Class<?> cls2, Bundle bundle, int i) {
        if (!m4892n1()) {
            Intent intent = new Intent(this.f73786d, cls);
            intent.putExtras(bundle);
            this.f73786d.startActivity(intent);
            ((Activity) this.f73786d).overridePendingTransition(C4804R.anim.f85970from_fade_in, C4804R.anim.f85971from_fade_out);
            return;
        }
        try {
            Fragment fragment = (Fragment) cls2.getConstructor(new Class[0]).newInstance(new Object[0]);
            fragment.m44751k2(bundle);
            if (i == C4804R.C4808id.container) {
                m4986K1(f73780u, fragment, C4804R.C4808id.f87010rootcontainer, true, true);
            } else {
                m4986K1(f73781v, fragment, i, false, true);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("OpenFragment", "Error in creating fragment : " + e.toString());
            FirebaseCrashlytics.m18030d().m18027g(e);
            e.printStackTrace();
        }
    }

    /* renamed from: M0 */
    public Observable<HttpURLConnection> m4981M0(final String str, final String str2, final String str3, final String str4, final Bundle bundle) {
        final String str5 = str2 + ".download";
        return Observable.m7156x1(new ObservableOnSubscribe<HttpURLConnection>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.4
            /* JADX WARN: Code restructure failed: missing block: B:13:0x00d7, code lost:
                if (new java.io.File(r3).exists() == false) goto L19;
             */
            /* JADX WARN: Code restructure failed: missing block: B:14:0x00d9, code lost:
                new java.io.File(r3).delete();
             */
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void mo3518a(@io.reactivex.rxjava3.annotations.NonNull io.reactivex.rxjava3.core.ObservableEmitter<java.net.HttpURLConnection> r12) throws java.lang.Throwable {
                /*
                    Method dump skipped, instructions count: 642
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: net.imedicaldoctor.imd.Data.CompressHelper.C36704.mo3518a(io.reactivex.rxjava3.core.ObservableEmitter):void");
            }
        });
    }

    /* renamed from: M1 */
    public byte[] m4980M1(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[(int) new File(str).length()];
            IOUtils.readFully(fileInputStream, bArr);
            fileInputStream.close();
            return bArr;
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            String cls = getClass().toString();
            iMDLogger.m3294f(cls, "Error in Reading file : " + str);
            return null;
        }
    }

    /* renamed from: N */
    public void m4979N(Class<?> cls, Class<?> cls2, Bundle bundle) {
        m4982M(cls, cls2, bundle, C4804R.C4808id.container);
    }

    /* renamed from: N0 */
    public void m4978N0() {
        if (m4917f2().booleanValue()) {
            return;
        }
        m4933b2(null);
        Process.killProcess(Process.myPid());
    }

    /* renamed from: N1 */
    public String m4977N1(String str) {
        try {
            return FileUtils.readFileToString(new File(str), "UTF-8");
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            e.printStackTrace();
            return "";
        }
    }

    /* renamed from: O */
    public void m4976O(Class<?> cls, Class<?> cls2, Bundle bundle) {
        m4982M(cls, cls2, bundle, C4804R.C4808id.f86865detail_container);
        SlidingPaneLayout slidingPaneLayout = (SlidingPaneLayout) ((Activity) this.f73786d).findViewById(C4804R.C4808id.f87019sliding_layout);
        if (slidingPaneLayout != null) {
            slidingPaneLayout.m42184c();
        }
    }

    /* renamed from: O0 */
    public void m4975O0() {
        m4871u1();
    }

    /* renamed from: O1 */
    public int m4974O1() {
        Bundle m4907i1 = m4907i1(m4946Y(m4971P1(), "Select c from r where id=1"));
        if (m4907i1 == null) {
            m4885q(m4971P1(), "insert into r values (1,0)");
            return 0;
        }
        return Integer.valueOf(m4907i1.getString("c")).intValue();
    }

    /* renamed from: P */
    public void m4973P(String str) {
        this.f73786d.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(str)));
    }

    /* renamed from: P0 */
    public String m4972P0() {
        String str = m4856z1() + "/favorites.db";
        if (!new File(str).exists()) {
            try {
                SQLiteDatabase.openOrCreateDatabase(str, (SQLiteDatabase.CursorFactory) null).execSQL("CREATE TABLE favorites (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , dbName TEXT, dbTitle TEXT, dbAddress TEXT, dbDate TEXT, dbDocName TEXT);");
            } catch (Exception e) {
                FirebaseCrashlytics.m18030d().m18027g(e);
                new File(str).delete();
                try {
                    SQLiteDatabase.openOrCreateDatabase(str, (SQLiteDatabase.CursorFactory) null).execSQL("CREATE TABLE favorites (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , dbName TEXT, dbTitle TEXT, dbAddress TEXT, dbDate TEXT, dbDocName TEXT);");
                } catch (Exception unused) {
                }
            }
        }
        return str;
    }

    /* renamed from: P1 */
    public String m4971P1() {
        String str = m4856z1() + "/recent.db";
        if (!new File(str).exists()) {
            SQLiteDatabase openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(str, (SQLiteDatabase.CursorFactory) null);
            openOrCreateDatabase.execSQL("create table recent (id integer primary key autoincrement, dbName text, dbTitle text, dbAddress text, dbDate text, dbDocName text);");
            openOrCreateDatabase.execSQL("create table dbrecent (id integer primary key autoincrement, dbName text, dbTitle text, dbDate text,dbIcon text);");
            openOrCreateDatabase.execSQL("create table r (id integer primary key autoincrement, c int)");
        }
        if (m4907i1(m4946Y(str, "SELECT name FROM sqlite_master WHERE type='table' AND name='r'")) == null) {
            SQLiteDatabase.openOrCreateDatabase(str, (SQLiteDatabase.CursorFactory) null).execSQL("create table r (id integer primary key autoincrement, c int)");
        }
        return str;
    }

    /* renamed from: Q */
    public String m4970Q(String str) {
        return m4873u(str).equals(m4873u(m4856z1())) ? "Internal Storage" : "External Storage";
    }

    /* renamed from: Q0 */
    public Bundle m4969Q0(final String str, final String str2) {
        ArrayList<Bundle> arrayList = ((iMD) this.f73786d.getApplicationContext()).f83461s;
        if ((arrayList.size() == 0) || (arrayList == null)) {
            return null;
        }
        ArrayList arrayList2 = new ArrayList(Collections2.m23110e(arrayList, new Predicate<Bundle>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.12
            @Override // com.google.common.base.Predicate
            /* renamed from: a */
            public boolean apply(Bundle bundle) {
                try {
                    return bundle.getString(str).equals(str2);
                } catch (Exception e) {
                    FirebaseCrashlytics.m18030d().m18027g(e);
                    return false;
                }
            }
        }));
        if (arrayList2.size() == 0) {
            return null;
        }
        return (Bundle) arrayList2.get(0);
    }

    /* renamed from: Q1 */
    public void m4968Q1(File[] fileArr) {
        if (fileArr != null) {
            int i = 0;
            while (i != fileArr.length) {
                String absolutePath = fileArr[i].getAbsolutePath();
                if (absolutePath.contains(BuildConfig.f73635b) || absolutePath.contains("Documents/iMD") || absolutePath.contains("Documents/.iMD")) {
                    i++;
                } else {
                    String str = fileArr[i].getAbsolutePath() + "||" + fileArr[i].length() + "||" + fileArr[i].lastModified();
                    if (this.f73785c != null) {
                        str = this.f73785c + "\n" + str;
                    }
                    this.f73785c = str;
                    if (fileArr[i].isDirectory()) {
                        m4968Q1(fileArr[i].listFiles());
                    }
                    i++;
                    iMDLogger.m3296d(i + "", absolutePath);
                }
            }
        }
    }

    /* renamed from: R */
    public String m4967R() {
        String str = m4856z1() + "/psych.db";
        if (!new File(str).exists()) {
            try {
                SQLiteDatabase openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(str, (SQLiteDatabase.CursorFactory) null);
                openOrCreateDatabase.execSQL("CREATE TABLE tol (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , name TEXT, time integer, moves integer, dateadded TEXT)");
                openOrCreateDatabase.execSQL("CREATE TABLE igt (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , name TEXT, money integer, time integer, a integer,b integer,c integer, d integer,pos int,neg int, dateadded TEXT)");
            } catch (Exception e) {
                FirebaseCrashlytics.m18030d().m18027g(e);
            }
        }
        return str;
    }

    /* renamed from: R0 */
    public ArrayList<Bundle> m4966R0(final String str, final String str2) {
        return new ArrayList<>(Collections2.m23110e(((iMD) this.f73786d.getApplicationContext()).f83461s, new Predicate<Bundle>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.13
            @Override // com.google.common.base.Predicate
            /* renamed from: a */
            public boolean apply(Bundle bundle) {
                try {
                    return bundle.getString(str).equals(str2);
                } catch (Exception e) {
                    FirebaseCrashlytics.m18030d().m18027g(e);
                    iMDLogger.m3294f("Error in filtering", e.getLocalizedMessage());
                    return false;
                }
            }
        }));
    }

    /* renamed from: S */
    public Cursor m4964S(Bundle bundle, String str) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        return m4958U(m4948X0(bundle), str);
    }

    /* renamed from: S0 */
    public String m4963S0(String str) {
        return str.replace("'", "''");
    }

    /* renamed from: S1 */
    public void m4962S1() {
        String m4971P1;
        String str;
        if (m4907i1(m4946Y(m4971P1(), "Select c from r where id=1")) == null) {
            m4971P1 = m4971P1();
            str = "insert into r values (1,0)";
        } else {
            m4971P1 = m4971P1();
            str = "update r set c=0 where id=1";
        }
        m4885q(m4971P1, str);
    }

    /* renamed from: T */
    public Cursor m4961T(Bundle bundle, String str, String str2) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        return m4958U(m4945Y0(bundle, str2), str);
    }

    /* renamed from: T0 */
    public String m4960T0(String str) {
        return str.replace("&amp;", "&");
    }

    /* renamed from: T1 */
    public String m4959T1() {
        try {
            return new VBHelper(this.f73786d).m3424i(FileUtils.readFileToString(new File(m4856z1() + "/exp.txt"), "UTF-8"), "127");
        } catch (Exception unused) {
            return "";
        }
    }

    /* renamed from: U */
    public Cursor m4958U(String str, String str2) {
        try {
            return this.f73786d.getContentResolver().query(Uri.parse("content://net.imedicaldoctor.imd/query"), null, str, null, str2);
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            e.printStackTrace();
            iMDLogger.m3294f("QueryDB " + str, "Error in Query DB , " + str + ", " + e.getLocalizedMessage());
            return null;
        }
    }

    /* renamed from: U1 */
    public Bundle m4956U1(Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        Bundle bundle = new Bundle();
        for (int i = 0; i < columnCount; i++) {
            String string = cursor.getString(i);
            if (string == null) {
                string = "";
            }
            bundle.putString(cursor.getColumnName(i), string);
        }
        return bundle;
    }

    /* renamed from: V */
    public ArrayList<Bundle> m4955V(Bundle bundle, String str) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        return m4946Y(m4948X0(bundle), str);
    }

    /* renamed from: V1 */
    public void m4953V1(int i) {
        if (i == 127) {
            m4962S1();
        }
    }

    /* renamed from: W */
    public ArrayList<Bundle> m4952W(Bundle bundle, String str, String str2) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        return m4946Y(m4945Y0(bundle, str2), str);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x005d, code lost:
        if (r14.equals(com.google.android.exoplayer2.metadata.icy.IcyHeaders.f35463C2) != false) goto L11;
     */
    /* renamed from: W1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void m4950W1(java.util.ArrayList<android.os.Bundle> r17) {
        /*
            r16 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = r16.m4856z1()
            r0.append(r1)
            java.lang.String r1 = "/databases.json"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            org.json.JSONArray r2 = new org.json.JSONArray
            r2.<init>()
            r4 = 0
        L20:
            int r5 = r17.size()
            if (r4 >= r5) goto Lb7
            r5 = r17
            java.lang.Object r6 = r5.get(r4)
            android.os.Bundle r6 = (android.os.Bundle) r6
            java.lang.String r7 = "items"
            java.util.ArrayList r8 = r6.getParcelableArrayList(r7)
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            org.json.JSONArray r10 = new org.json.JSONArray
            r10.<init>()
            r11 = 0
        L3f:
            int r12 = r8.size()
            if (r11 >= r12) goto L8d
            java.lang.Object r12 = r8.get(r11)
            android.os.Bundle r12 = (android.os.Bundle) r12
            java.lang.String r13 = "dontSearch"
            boolean r14 = r12.containsKey(r13)
            if (r14 == 0) goto L60
            java.lang.String r14 = r12.getString(r13)
            java.lang.String r15 = "1"
            boolean r14 = r14.equals(r15)
            if (r14 == 0) goto L60
            goto L62
        L60:
            java.lang.String r15 = "0"
        L62:
            android.os.Bundle r14 = new android.os.Bundle
            r14.<init>()
            java.lang.String r3 = "Name"
            java.lang.String r5 = r12.getString(r3)
            r14.putString(r3, r5)
            r14.putString(r13, r15)
            r9.add(r14)
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch: java.lang.Exception -> L88
            r5.<init>()     // Catch: java.lang.Exception -> L88
            java.lang.String r12 = r12.getString(r3)     // Catch: java.lang.Exception -> L88
            r5.put(r3, r12)     // Catch: java.lang.Exception -> L88
            r5.put(r13, r15)     // Catch: java.lang.Exception -> L88
            r10.put(r5)     // Catch: java.lang.Exception -> L88
        L88:
            int r11 = r11 + 1
            r5 = r17
            goto L3f
        L8d:
            android.os.Bundle r3 = new android.os.Bundle
            r3.<init>()
            java.lang.String r5 = "title"
            java.lang.String r8 = r6.getString(r5)
            r3.putString(r5, r8)
            r3.putParcelableArrayList(r7, r9)
            r1.add(r3)
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch: java.lang.Exception -> Lb3
            r3.<init>()     // Catch: java.lang.Exception -> Lb3
            java.lang.String r6 = r6.getString(r5)     // Catch: java.lang.Exception -> Lb3
            r3.put(r5, r6)     // Catch: java.lang.Exception -> Lb3
            r3.put(r7, r10)     // Catch: java.lang.Exception -> Lb3
            r2.put(r3)     // Catch: java.lang.Exception -> Lb3
        Lb3:
            int r4 = r4 + 1
            goto L20
        Lb7:
            java.lang.String r1 = r2.toString()
            java.io.File r2 = new java.io.File
            r2.<init>(r0)
            boolean r2 = r2.exists()
            if (r2 == 0) goto Lce
            java.io.File r2 = new java.io.File
            r2.<init>(r0)
            r2.delete()
        Lce:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Exception -> Ldf
            java.io.File r3 = new java.io.File     // Catch: java.lang.Exception -> Ldf
            r3.<init>(r0)     // Catch: java.lang.Exception -> Ldf
            r2.<init>(r3)     // Catch: java.lang.Exception -> Ldf
            org.apache.commons.p024io.IOUtils.write(r1, r2)     // Catch: java.lang.Exception -> Ldf
            r2.close()     // Catch: java.lang.Exception -> Ldf
            goto Lf0
        Ldf:
            r0 = move-exception
            com.google.firebase.crashlytics.FirebaseCrashlytics r1 = com.google.firebase.crashlytics.FirebaseCrashlytics.m18030d()
            r1.m18027g(r0)
            java.lang.String r1 = "Error in writing json"
            java.lang.String r0 = r0.getLocalizedMessage()
            net.imedicaldoctor.imd.iMDLogger.m3294f(r1, r0)
        Lf0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: net.imedicaldoctor.imd.Data.CompressHelper.m4950W1(java.util.ArrayList):void");
    }

    /* renamed from: X */
    public ArrayList<Bundle> m4949X(Bundle bundle, String str, String str2, boolean z) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        ArrayList<Bundle> m4952W = m4952W(bundle, str, str2);
        return (m4952W == null && z) ? new ArrayList<>() : m4952W;
    }

    /* renamed from: X1 */
    public ArrayList<Bundle> m4947X1() {
        Log.e("Speed", "Sections Started");
        ArrayList<Bundle> m4859y1 = m4859y1();
        m4950W1(m4859y1);
        ArrayList<Bundle> arrayList = new ArrayList<>();
        Iterator<Bundle> it2 = m4859y1.iterator();
        while (it2.hasNext()) {
            Iterator it3 = it2.next().getParcelableArrayList(FirebaseAnalytics.Param.f55203f0).iterator();
            while (it3.hasNext()) {
                arrayList.add((Bundle) it3.next());
            }
        }
        f73778s = arrayList;
        Log.e("Speed", "Sections ended");
        return m4859y1;
    }

    /* renamed from: Y */
    public ArrayList<Bundle> m4946Y(String str, String str2) {
        try {
            Cursor query = this.f73786d.getContentResolver().query(Uri.parse("content://net.imedicaldoctor.imd/query"), null, str, null, str2);
            ArrayList<Bundle> m4932c = m4932c(query);
            query.close();
            m4916g(str);
            return m4932c;
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("QueryDBAsArray " + str, "Error in Query DB , " + str + ", " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: Y1 */
    public ArrayList<Bundle> m4944Y1(ArrayList<Bundle> arrayList) {
        return m4941Z1(arrayList, "Section");
    }

    /* renamed from: Z */
    public ArrayList<String> m4943Z(String str, String str2, String str3) {
        try {
            Cursor query = this.f73786d.getContentResolver().query(Uri.parse("content://net.imedicaldoctor.imd/query"), null, str, null, str2);
            ArrayList<String> m4928d = m4928d(query, str3);
            query.close();
            m4916g(str);
            return m4928d;
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("QueryDBAsArray " + str, "Error in Query DB , " + str + ", " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: Z1 */
    public ArrayList<Bundle> m4941Z1(ArrayList<Bundle> arrayList, String str) {
        ArrayList<Bundle> arrayList2 = new ArrayList<>();
        String str2 = null;
        if (arrayList == null) {
            return null;
        }
        ArrayList<? extends Parcelable> arrayList3 = new ArrayList<>();
        Iterator<Bundle> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            Bundle next = it2.next();
            String string = next.getString(str);
            if (str2 == null) {
                arrayList3.add(next);
            } else if (str2.equals(string)) {
                arrayList3.add(next);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("title", str2);
                bundle.putParcelableArrayList(FirebaseAnalytics.Param.f55203f0, arrayList3);
                arrayList2.add(bundle);
                ArrayList<? extends Parcelable> arrayList4 = new ArrayList<>();
                arrayList4.add(next);
                arrayList3 = arrayList4;
            }
            str2 = string;
        }
        if (str2 != null) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("title", str2);
            bundle2.putParcelableArrayList(FirebaseAnalytics.Param.f55203f0, arrayList3);
            arrayList2.add(bundle2);
        }
        return arrayList2;
    }

    /* renamed from: a */
    public String m4940a() {
        StringBuilder sb;
        String str;
        VBHelper vBHelper = this.f73788f;
        String[] split = TextUtils.split(vBHelper.m3410w(vBHelper.m3421l()).replace("||", "::"), "::");
        ArrayList arrayList = new ArrayList(Arrays.asList(TextUtils.split(split[3], ",")));
        String str2 = split[2];
        if (arrayList.contains(TtmlNode.f38128r0)) {
            if (str2.length() <= 0) {
                return "All";
            }
            sb = new StringBuilder();
            str = "Active|";
        } else if (!arrayList.contains("expired")) {
            return "Simple";
        } else {
            sb = new StringBuilder();
            str = "Expired|";
        }
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    /* renamed from: a0 */
    public Observable<ArrayList<Bundle>> m4939a0(final Bundle bundle, final String str) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        return Observable.m7156x1(new ObservableOnSubscribe<ArrayList<Bundle>>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.21
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<ArrayList<Bundle>> observableEmitter) throws Throwable {
                ArrayList<Bundle> m4955V = CompressHelper.this.m4955V(bundle, str);
                if (m4955V != null) {
                    observableEmitter.onNext(m4955V);
                }
                observableEmitter.onComplete();
            }
        });
    }

    /* renamed from: a1 */
    public Bundle m4938a1(final String str) {
        ArrayList arrayList = new ArrayList(Collections2.m23110e(((iMD) this.f73786d.getApplicationContext()).f83461s, new Predicate<Bundle>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.25
            @Override // com.google.common.base.Predicate
            /* renamed from: a */
            public boolean apply(Bundle bundle) {
                return bundle.getString("Name").equals(str);
            }
        }));
        if (arrayList.size() == 0) {
            return null;
        }
        return (Bundle) arrayList.get(0);
    }

    /* renamed from: a2 */
    public ArrayList<Bundle> m4937a2(ArrayList<Bundle> arrayList, String str) {
        ArrayList<Bundle> arrayList2 = new ArrayList<>();
        if (arrayList == null) {
            return null;
        }
        ArrayList<? extends Parcelable> arrayList3 = new ArrayList<>();
        Iterator<Bundle> it2 = arrayList.iterator();
        String str2 = "";
        while (it2.hasNext()) {
            Bundle next = it2.next();
            String lowerCase = next.getString(str).substring(0, 1).toLowerCase();
            String upperCase = !"abcdefghijklmnopqrstuvwxyz".contains(lowerCase) ? "#" : lowerCase.toUpperCase();
            if (str2.length() != 0) {
                if (str2.equals(upperCase)) {
                    arrayList3.add(next);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", str2);
                    bundle.putParcelableArrayList(FirebaseAnalytics.Param.f55203f0, arrayList3);
                    arrayList2.add(bundle);
                    arrayList3 = new ArrayList<>();
                }
            }
            arrayList3.add(next);
            str2 = upperCase;
        }
        if (str2.length() > 0) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("title", str2);
            bundle2.putParcelableArrayList(FirebaseAnalytics.Param.f55203f0, arrayList3);
            arrayList2.add(bundle2);
        }
        return arrayList2;
    }

    /* renamed from: b0 */
    public Observable<ArrayList<Bundle>> m4935b0(final Bundle bundle, final String str, final String str2) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        return Observable.m7156x1(new ObservableOnSubscribe<ArrayList<Bundle>>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.23
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<ArrayList<Bundle>> observableEmitter) throws Throwable {
                ArrayList<Bundle> m4952W = CompressHelper.this.m4952W(bundle, str, str2);
                if (m4952W != null) {
                    observableEmitter.onNext(m4952W);
                }
                observableEmitter.onComplete();
            }
        });
    }

    /* renamed from: b1 */
    public Bundle m4934b1(final String str) {
        ArrayList arrayList = new ArrayList(Collections2.m23110e(((iMD) this.f73786d.getApplicationContext()).f83461s, new Predicate<Bundle>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.26
            @Override // com.google.common.base.Predicate
            /* renamed from: a */
            public boolean apply(Bundle bundle) {
                return bundle.getString("Title").equals(str);
            }
        }));
        if (arrayList.size() == 0) {
            return null;
        }
        return (Bundle) arrayList.get(0);
    }

    /* renamed from: b2 */
    public void m4933b2(String str) {
        if (str == null) {
            PreferenceManager.getDefaultSharedPreferences(this.f73786d).edit().remove("ActivationCode").commit();
        }
        PreferenceManager.getDefaultSharedPreferences(this.f73786d).edit().putString("ActivationCode", str).commit();
    }

    /* renamed from: c */
    public ArrayList<Bundle> m4932c(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<Bundle> arrayList = new ArrayList<>(cursor.getCount());
            int columnCount = cursor.getColumnCount();
            do {
                Bundle bundle = new Bundle();
                for (int i = 0; i < columnCount; i++) {
                    if (cursor.getType(i) == 4) {
                        bundle.putByteArray(cursor.getColumnName(i), cursor.getBlob(i));
                    } else {
                        String string = cursor.getString(i);
                        if (string == null) {
                            string = "";
                        }
                        bundle.putString(cursor.getColumnName(i), string);
                    }
                }
                arrayList.add(bundle);
            } while (cursor.moveToNext());
            cursor.close();
            return arrayList;
        }
        return null;
    }

    /* renamed from: c0 */
    public Observable<ArrayList<Bundle>> m4931c0(final String str, final String str2) {
        return Observable.m7156x1(new ObservableOnSubscribe<ArrayList<Bundle>>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.22
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<ArrayList<Bundle>> observableEmitter) throws Throwable {
                ArrayList<Bundle> m4946Y = CompressHelper.this.m4946Y(str, str2);
                if (m4946Y != null) {
                    observableEmitter.onNext(m4946Y);
                }
                observableEmitter.onComplete();
            }
        });
    }

    /* renamed from: c2 */
    public void m4929c2(String str, String str2) {
        Intent intent = new Intent("android.intent.action.SEND");
        File file = new File(str);
        if (file.exists()) {
            intent.setType(str2);
            Context context = this.f73786d;
            Uri m47727e = FileProvider.m47727e(context, this.f73786d.getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra("android.intent.extra.STREAM", m47727e);
            intent.addFlags(1);
            Intent createChooser = Intent.createChooser(intent, "Share File");
            for (ResolveInfo resolveInfo : this.f73786d.getPackageManager().queryIntentActivities(createChooser, 65536)) {
                this.f73786d.grantUriPermission(resolveInfo.activityInfo.packageName, m47727e, 3);
            }
            this.f73786d.startActivity(createChooser);
        }
    }

    /* renamed from: d */
    public ArrayList<String> m4928d(Cursor cursor, String str) {
        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<String> arrayList = new ArrayList<>(cursor.getCount());
            int columnIndex = cursor.getColumnIndex(str);
            do {
                new Bundle();
                arrayList.add(cursor.getString(columnIndex));
            } while (cursor.moveToNext());
            cursor.close();
            return arrayList;
        }
        return null;
    }

    /* renamed from: d0 */
    public ArrayList<Bundle> m4927d0(String str, String str2, int i) {
        try {
            Cursor query = this.f73786d.getContentResolver().query(Uri.parse("content://net.imedicaldoctor.imd/query"), null, str, null, str2);
            ArrayList<Bundle> m4924e = m4924e(query, i);
            query.close();
            m4916g(str);
            return m4924e;
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("QueryDBAsArray " + str, "Error in Query DB , " + str + ", " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: d1 */
    public Bundle m4926d1(ArrayList<Bundle> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        return arrayList.get(0);
    }

    /* renamed from: d2 */
    public void m4925d2(String str) {
        new AlertDialog.Builder(this.f73786d, C4804R.style.f88094alertDialogTheme).mo26292l(str).mo26284p("OK", new DialogInterface.OnClickListener() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.24
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).m52864I();
    }

    /* renamed from: e */
    public ArrayList<Bundle> m4924e(Cursor cursor, int i) {
        if (cursor != null && cursor.moveToFirst()) {
            ArrayList<Bundle> arrayList = new ArrayList<>(cursor.getCount());
            int columnCount = cursor.getColumnCount();
            int count = cursor.getCount();
            for (int i2 = 0; i2 < count; i2++) {
                Bundle bundle = new Bundle();
                if (i2 < i) {
                    for (int i3 = 0; i3 < columnCount; i3++) {
                        if (cursor.getType(i3) == 4) {
                            bundle.putByteArray(cursor.getColumnName(i3), cursor.getBlob(i3));
                        } else {
                            String string = cursor.getString(i3);
                            if (string == null) {
                                string = "";
                            }
                            bundle.putString(cursor.getColumnName(i3), string);
                        }
                    }
                    arrayList.add(bundle);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayList;
        }
        return null;
    }

    /* renamed from: e0 */
    public Bundle m4923e0(Bundle bundle, String str) {
        return m4926d1(m4955V(bundle, str));
    }

    /* renamed from: e1 */
    public HashSet<String> m4922e1() {
        if (((iMD) this.f73786d.getApplicationContext()).f83458X != null) {
            return ((iMD) this.f73786d.getApplicationContext()).f83458X;
        }
        HashSet<String> m4930c1 = m4930c1();
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(m4856z1());
        File[] externalFilesDirs = this.f73786d.getExternalFilesDirs(null);
        m4930c1.add("/storage/sdcard1");
        m4930c1.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        Iterator<String> it2 = m4930c1.iterator();
        while (it2.hasNext()) {
            String str = it2.next() + "/Android/data/net.imedicaldoctor.imd/Documents";
            new File(str).mkdirs();
            if (new File(str).exists() && !hashSet.contains(str)) {
                hashSet.add(str);
            }
        }
        Iterator<String> it3 = m4930c1.iterator();
        while (it3.hasNext()) {
            String str2 = it3.next() + "/Android/data/net.imedicaldoctor.imd/.Documents";
            new File(str2).mkdirs();
            if (new File(str2).exists() && !hashSet.contains(str2)) {
                hashSet.add(str2);
            }
        }
        int i = 0;
        if (externalFilesDirs != null) {
            try {
                for (File file : externalFilesDirs) {
                    if (file != null) {
                        String str3 = file.getParentFile().getAbsolutePath() + "/Documents";
                        new File(str3).mkdirs();
                        if (new File(str3).exists() && !hashSet.contains(str3)) {
                            hashSet.add(str3);
                        }
                    }
                }
                for (File file2 : externalFilesDirs) {
                    if (file2 != null) {
                        String str4 = file2.getParentFile().getAbsolutePath() + "/.Documents";
                        new File(str4).mkdirs();
                        if (new File(str4).exists() && !hashSet.contains(str4)) {
                            hashSet.add(str4);
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
        HashSet hashSet2 = new HashSet();
        ArrayList arrayList = new ArrayList();
        Iterator<String> it4 = hashSet.iterator();
        while (it4.hasNext()) {
            arrayList.add(it4.next());
        }
        while (i < arrayList.size() - 1) {
            int i2 = i + 1;
            for (int i3 = i2; i3 < arrayList.size(); i3++) {
                try {
                    if (new File((String) arrayList.get(i)).getCanonicalPath().equals(new File((String) arrayList.get(i3)).getCanonicalPath())) {
                        hashSet2.add((String) arrayList.get(i3));
                    }
                } catch (Exception unused2) {
                }
            }
            i = i2;
        }
        Iterator it5 = hashSet2.iterator();
        while (it5.hasNext()) {
            hashSet.remove((String) it5.next());
        }
        ((iMD) this.f73786d.getApplicationContext()).f83458X = hashSet;
        return hashSet;
    }

    /* renamed from: f0 */
    public Bundle m4919f0(Bundle bundle, String str, String str2) {
        return m4926d1(m4952W(bundle, str, str2));
    }

    /* renamed from: f1 */
    public String m4918f1(String str, String str2) throws Exception {
        String m4866w0 = m4866w0(str2, str2);
        if (m4866w0 != null) {
            return m4866w0;
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(180000);
        if (httpURLConnection.getResponseCode() == 200) {
            httpURLConnection.getHeaderField(HttpHeaders.f53950W);
            httpURLConnection.getContentType();
            httpURLConnection.getContentLength();
            String iOUtils = IOUtils.toString(httpURLConnection.getInputStream());
            m4875t0(str2, iOUtils, str2);
            httpURLConnection.disconnect();
            return iOUtils;
        }
        throw new Exception("Error in contacting server");
    }

    /* renamed from: f2 */
    public Boolean m4917f2() {
        if (this.f73788f.m3416q() == "") {
            return Boolean.TRUE;
        }
        VBHelper vBHelper = this.f73788f;
        String[] split = TextUtils.split(vBHelper.m3410w(vBHelper.m3421l()).replace("||", "::"), "::");
        if (split.length < 12) {
            m4913g2("Parts less than 12 . parts length = " + split.length);
            return Boolean.FALSE;
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(TextUtils.split(split[3], ",")));
        String str = split[8];
        String m4874t1 = m4874t1("NSDate" + split[2] + split[3] + split[10] + split[9] + "NSString");
        if (!m4874t1("NSDate" + m4874t1).equals(str)) {
            m4913g2("Hash Validation Failed");
            return Boolean.FALSE;
        }
        Log.d("Validation", "Check hash passed");
        Date date = new Date(Long.parseLong(split[10]) * 1000);
        TimeUnit timeUnit = TimeUnit.HOURS;
        TimeUnit timeUnit2 = TimeUnit.MILLISECONDS;
        long convert = timeUnit.convert(new Date().getTime() - date.getTime(), timeUnit2);
        if (convert > 240) {
            m4913g2("Server Date and System Date Mismatch . Server Date : " + date.getTime() + ", System Date : " + new Date().getTime());
            return Boolean.FALSE;
        } else if (convert < -240) {
            m4913g2("Server Date and System Date Mismatch . Server Date : " + date.getTime() + ", System Date : " + new Date().getTime());
            return Boolean.FALSE;
        } else {
            Log.d("Validation", "Check server date passed");
            String str2 = split[2];
            if (str2.length() > 2) {
                long convert2 = TimeUnit.DAYS.convert(new Date(Long.parseLong(str2) * 1000).getTime() - new Date().getTime(), timeUnit2);
                if (convert2 < 0) {
                    if (!arrayList.contains("expired")) {
                        m4913g2("VIP Account Expired. Days Remaining : " + convert2);
                        return Boolean.FALSE;
                    } else if (arrayList.contains(TtmlNode.f38128r0)) {
                        m4913g2("VIP Account Expired. Days Remaining : " + convert2);
                        return Boolean.FALSE;
                    }
                }
            }
            Log.d("Validation", "Check DOE passed");
            if (split[11].equals("0")) {
                m4899l0();
            }
            int m4974O1 = m4974O1();
            Log.d("Validation", "C = " + m4974O1);
            if (m4974O1 > 5000) {
                m4913g2("C Exceeded 5000. Counter : " + m4974O1);
                m4953V1(127);
                return Boolean.FALSE;
            }
            long convert3 = timeUnit.convert(new Date().getTime() - m4983L1(), timeUnit2);
            if (convert3 >= -48) {
                Log.d("Validation", "D Succeed");
                return Boolean.TRUE;
            }
            m4913g2("D Failed. Hours : " + convert3);
            Log.d("Validation", "D failed . hours = " + convert3);
            return Boolean.FALSE;
        }
    }

    /* renamed from: g */
    public void m4916g(String str) {
        this.f73786d.getContentResolver().query(Uri.parse("content://net.imedicaldoctor.imd/close"), null, str, null, null);
    }

    /* renamed from: g0 */
    public Observable<Cursor> m4915g0(final Bundle bundle, final String str) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        return Observable.m7156x1(new ObservableOnSubscribe<Cursor>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.19
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<Cursor> observableEmitter) throws Throwable {
                Cursor m4964S = CompressHelper.this.m4964S(bundle, str);
                if (m4964S != null) {
                    observableEmitter.onNext(m4964S);
                }
                observableEmitter.onComplete();
            }
        });
    }

    /* renamed from: g2 */
    public void m4913g2(String str) {
        try {
            FileUtils.writeStringToFile(new File(m4856z1() + "/exp.txt"), new VBHelper(this.f73786d).m3420m(str, "127"), "UTF-8");
        } catch (Exception unused) {
        }
    }

    /* renamed from: h */
    public Cursor m4912h(ArrayList<Bundle> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        String[] strArr = (String[]) FluentIterable.m22810v(arrayList.get(0).keySet()).m22837H(String.class);
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        Iterator<Bundle> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            Bundle next = it2.next();
            ArrayList arrayList2 = new ArrayList();
            for (String str : strArr) {
                arrayList2.add(next.getString(str));
            }
            matrixCursor.addRow(arrayList2);
        }
        return matrixCursor;
    }

    /* renamed from: h0 */
    public Observable<Cursor> m4911h0(final Bundle bundle, final String str, final String str2) {
        if (bundle.containsKey("Damu")) {
            str = m4860y0(str);
        }
        return Observable.m7156x1(new ObservableOnSubscribe<Cursor>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.18
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<Cursor> observableEmitter) throws Throwable {
                Cursor m4961T = CompressHelper.this.m4961T(bundle, str, str2);
                if (m4961T != null) {
                    observableEmitter.onNext(m4961T);
                }
                observableEmitter.onComplete();
            }
        });
    }

    /* renamed from: h1 */
    public JSONObject m4910h1(JSONArray jSONArray, String str, String str2) {
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject.getString(str).equals(str2)) {
                    return jSONObject;
                }
            } catch (Exception e) {
                FirebaseCrashlytics.m18030d().m18027g(e);
                iMDLogger.m3294f("Error in getJSONObjectFromArray", e.getLocalizedMessage());
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /* renamed from: i */
    public Cursor m4909i(ArrayList<Bundle> arrayList, String[] strArr) {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        arrayList.get(0);
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        Iterator<Bundle> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            Bundle next = it2.next();
            ArrayList arrayList2 = new ArrayList();
            for (String str : strArr) {
                arrayList2.add(next.getString(str));
            }
            matrixCursor.addRow(arrayList2);
        }
        return matrixCursor;
    }

    /* renamed from: i0 */
    public Observable<Cursor> m4908i0(final String str, final String str2) {
        return Observable.m7156x1(new ObservableOnSubscribe<Cursor>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.20
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<Cursor> observableEmitter) throws Throwable {
                Cursor m4958U = CompressHelper.this.m4958U(str, str2);
                if (m4958U != null) {
                    observableEmitter.onNext(m4958U);
                }
                observableEmitter.onComplete();
            }
        });
    }

    /* renamed from: i1 */
    public Bundle m4907i1(ArrayList<Bundle> arrayList) {
        return m4858z(arrayList);
    }

    /* renamed from: j */
    public void m4906j(String str) {
        try {
            str = str.replace("//", "/");
            System.gc();
            File file = new File(str);
            file.exists();
            file.setWritable(true, false);
            if (Boolean.valueOf(file.delete()).booleanValue()) {
                Log.e("DeleteFile", "Delete Successfull . " + str);
            } else if (!Boolean.valueOf(file.getCanonicalFile().delete()).booleanValue()) {
                file.deleteOnExit();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            Log.e("DeleteFile", "Failed to Delete " + str);
        }
    }

    /* renamed from: j0 */
    public ArrayList<Bundle> m4905j0(String str) {
        return m4946Y(m4972P0(), str);
    }

    /* renamed from: j1 */
    public String m4904j1(String[] strArr) {
        return strArr[strArr.length - 1];
    }

    /* renamed from: k */
    public void m4903k(File file) {
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                m4903k(file2);
            }
        }
        file.delete();
    }

    /* renamed from: k0 */
    public void m4902k0() {
        File[] listFiles;
        Bundle m3430c;
        Log.e("Speed", "RefereshDatabaes Started");
        ArrayList<Bundle> arrayList = new ArrayList<>();
        if (this.f73788f.m3432a() == null) {
            ((iMD) this.f73786d.getApplicationContext()).f83461s = null;
            return;
        }
        Iterator<String> it2 = m4922e1().iterator();
        while (it2.hasNext()) {
            File file = new File(it2.next());
            if (file.listFiles() != null) {
                for (File file2 : file.listFiles()) {
                    if (file2.isDirectory()) {
                        File file3 = new File(file2.getAbsolutePath() + "/info.vbe");
                        if (file3.exists()) {
                            Bundle m3430c2 = this.f73788f.m3430c(file3);
                            if (m3430c2 != null) {
                                Bundle m4914g1 = m4914g1(arrayList, "Name", m3430c2.getString("Name"));
                                if (m4914g1 != null) {
                                    if (m4914g1.getString("Version").compareTo(m3430c2.getString("Version")) >= 0) {
                                        iMDLogger.m3294f("RefereshDatabases", "Delete Older Version");
                                        m4903k(file3.getParentFile());
                                    } else {
                                        iMDLogger.m3294f("RefereshDatabases", "There is an older version");
                                        String string = m4914g1.getString("Path");
                                        arrayList.remove(m4914g1);
                                        iMDLogger.m3294f("Deleting", string);
                                        m4903k(new File(string));
                                    }
                                }
                                m3430c2.putString("Path", file2.getAbsolutePath());
                                arrayList.add(m3430c2);
                            }
                        }
                        File file4 = new File(file2.getAbsolutePath() + "/info2.vbe");
                        if (file4.exists() && (m3430c = this.f73788f.m3430c(file4)) != null) {
                            m3430c.putString("Path", file2.getAbsolutePath());
                            arrayList.add(m3430c);
                        }
                    }
                }
            }
        }
        ((iMD) this.f73786d.getApplicationContext()).f83461s = arrayList;
        Log.e("Speed", "RefereshDatabaes Ended");
    }

    /* renamed from: l */
    public Observable<String> m4900l(Fragment fragment) {
        iMDLogger.m3294f("DownloadDBs", "Starting");
        return Observable.m7156x1(new ObservableOnSubscribe<String>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.1
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<String> observableEmitter) throws Throwable {
                if (CompressHelper.f73782w) {
                    iMDLogger.m3294f("DownloadDBs", "is Downloading , Return");
                    return;
                }
                do {
                } while (CompressHelper.f73782w);
                CompressHelper.f73782w = true;
                String str = CompressHelper.this.m5004E1() + "/DBs.z";
                if (new File(CompressHelper.this.m5004E1() + "/DBs.db").exists()) {
                    iMDLogger.m3294f("DownloadDBs", "DB Exist, no need to download");
                    CompressHelper.f73782w = false;
                    observableEmitter.onComplete();
                    return;
                }
                if (new File(str).exists()) {
                    new File(str).delete();
                }
                try {
                    iMDLogger.m3294f("DownloadDBs", "Downloading Zip File");
                    CompressHelper.this.m4990J0(CompressHelper.this.m4991J() + "/dbs.z", str);
                    iMDLogger.m3294f("DownloadDBs", "Downloading Zip File Completed");
                    Decompress decompress = new Decompress(str, CompressHelper.this.m5004E1() + "/", CompressHelper.this.f73786d);
                    iMDLogger.m3294f("CompressHelper", "Extract of dbs.z started");
                    boolean m4824i = decompress.m4824i();
                    iMDLogger.m3294f("CompressHelper", "Extract of dbs.z ended");
                    CompressHelper.f73782w = false;
                    if (m4824i) {
                        iMDLogger.m3294f("CompressHelper", "extract of dbs.z successful");
                        observableEmitter.onComplete();
                        return;
                    }
                    iMDLogger.m3294f("CompressHelper", "extract of dbs.z failed");
                    observableEmitter.onError(null);
                } catch (Exception e) {
                    CompressHelper.f73782w = false;
                    iMDLogger.m3294f("DownloadDBs", "Error in downloading file " + e.toString());
                    e.printStackTrace();
                    observableEmitter.onError(e);
                }
            }
        });
    }

    /* renamed from: l0 */
    public void m4899l0() {
        FirebaseMessaging.m16689u().m16686x().mo27521e(new OnCompleteListener<String>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.5
            @Override // com.google.android.gms.tasks.OnCompleteListener
            /* renamed from: a */
            public void mo4843a(@NonNull Task<String> task) {
                if (!task.mo27504v()) {
                    Log.w("RegisterToken", "Fetching FCM registration token failed", task.mo27509q());
                    return;
                }
                VBHelper vBHelper = new VBHelper(CompressHelper.this.f73786d);
                CompressHelper compressHelper = CompressHelper.this;
                compressHelper.m4890o0("PushRegistration|||||" + vBHelper.m3421l() + "|||||" + task.mo27508r()).m7300i6(Schedulers.m5370e()).m7193t4(AndroidSchedulers.m8490e()).m7319g6(new Consumer<String>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.5.1
                    @Override // io.reactivex.rxjava3.functions.Consumer
                    /* renamed from: a */
                    public void accept(String str) throws Throwable {
                        Log.e("RegisterToken", "Registration Successful");
                    }
                }, new Consumer<Throwable>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.5.2
                    @Override // io.reactivex.rxjava3.functions.Consumer
                    /* renamed from: a */
                    public void accept(Throwable th) throws Throwable {
                    }
                }, new Action() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.5.3
                    @Override // io.reactivex.rxjava3.functions.Action
                    public void run() throws Throwable {
                    }
                });
            }
        });
    }

    /* renamed from: m */
    public void m4897m(Bundle bundle, String str) {
        try {
            this.f73786d.getContentResolver().update(Uri.parse("content://net.imedicaldoctor.imd/query"), null, m4948X0(bundle), new String[]{str});
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("ExecuteDB" + bundle.getString("Name"), "Error in Executing DB , " + m4948X0(bundle) + ", " + e.getLocalizedMessage());
        }
    }

    /* renamed from: m0 */
    public void m4896m0(String str, String str2, String str3, String str4, Bundle bundle) {
        String str5;
        ArrayList arrayList = new ArrayList();
        String[] splitByWholeSeparatorPreserveAllTokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(str, ";;;");
        String[] splitByWholeSeparatorPreserveAllTokens2 = StringUtils.splitByWholeSeparatorPreserveAllTokens(str4, ",");
        int length = splitByWholeSeparatorPreserveAllTokens.length;
        int i = 0;
        while (i < length) {
            String[] splitByWholeSeparatorPreserveAllTokens3 = StringUtils.splitByWholeSeparatorPreserveAllTokens(splitByWholeSeparatorPreserveAllTokens[i], ":::");
            ArrayList arrayList2 = new ArrayList();
            int length2 = splitByWholeSeparatorPreserveAllTokens3.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length2) {
                String str6 = splitByWholeSeparatorPreserveAllTokens3[i2];
                String str7 = splitByWholeSeparatorPreserveAllTokens2[i3];
                String[] strArr = splitByWholeSeparatorPreserveAllTokens;
                if (bundle == null || !bundle.containsKey(str7)) {
                    str5 = "'" + str6.replace("'", "''") + "'";
                } else {
                    str5 = "'" + bundle.getString(str7).replace("'", "''") + "'";
                }
                arrayList2.add(str5);
                i3++;
                i2++;
                splitByWholeSeparatorPreserveAllTokens = strArr;
            }
            String[] strArr2 = splitByWholeSeparatorPreserveAllTokens;
            arrayList.add("Insert into " + str3 + " (" + str4 + ") values (" + StringUtils.join(arrayList2, ",") + ")");
            i++;
            splitByWholeSeparatorPreserveAllTokens = strArr2;
        }
        m4882r(str2, (String[]) arrayList.toArray(new String[0]), 0);
    }

    /* renamed from: m1 */
    public ArrayList<String> m4895m1() {
        HashSet<String> m4922e1 = m4922e1();
        Bundle bundle = new Bundle();
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator<String> it2 = m4922e1.iterator();
        while (it2.hasNext()) {
            String next = it2.next();
            long usableSpace = new File(next).getUsableSpace();
            if (!bundle.containsKey(String.valueOf(usableSpace))) {
                String replace = next.replace("Android/data/net.imedicaldoctor.imd/Documents", "").replace("Android/data/net.imedicaldoctor.imd/.Documents", "").replace("Documents/iMD", "").replace("Documents/.iMD", "");
                bundle.putString(String.valueOf(usableSpace), replace);
                arrayList.add(replace);
            }
        }
        return arrayList;
    }

    /* renamed from: n */
    public void m4894n(Bundle bundle, String str, String str2) {
        try {
            this.f73786d.getContentResolver().update(Uri.parse("content://net.imedicaldoctor.imd/query"), null, m4945Y0(bundle, str2), new String[]{str});
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("ExecuteDB" + bundle.getString("Name"), "Error in Executing DB , " + m4945Y0(bundle, str2) + ", " + e.getLocalizedMessage());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x010d  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x011c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v3, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r5v6, types: [java.net.HttpURLConnection, java.net.URLConnection] */
    /* renamed from: n0 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String m4893n0(java.lang.String r11) {
        /*
            Method dump skipped, instructions count: 312
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.imedicaldoctor.imd.Data.CompressHelper.m4893n0(java.lang.String):java.lang.String");
    }

    /* renamed from: n1 */
    public boolean m4892n1() {
        if (PreferenceManager.getDefaultSharedPreferences(this.f73786d).getBoolean("mobile", false)) {
            return false;
        }
        return this.f73786d.getResources().getBoolean(C4804R.bool.isTablet);
    }

    /* renamed from: o */
    public void m4891o(Bundle bundle, String str, String str2, int i) {
        try {
            this.f73786d.getContentResolver().update(Uri.parse("content://net.imedicaldoctor.imd/query"), null, m4945Y0(bundle, str2), new String[]{"SQLFile", str, String.valueOf(i)});
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("ExecuteDB" + bundle.getString("Name"), "Error in Executing DB , " + m4945Y0(bundle, str2) + ", " + e.getLocalizedMessage());
        }
    }

    /* renamed from: o0 */
    public Observable<String> m4890o0(final String str) {
        return Observable.m7156x1(new ObservableOnSubscribe<String>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.8
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<String> observableEmitter) throws Throwable {
                String m4893n0 = CompressHelper.this.m4893n0(str);
                if (m4893n0 == null) {
                    observableEmitter.onError(new Throwable("Error Occured"));
                    return;
                }
                String m3424i = CompressHelper.this.f73788f.m3424i(m4893n0, "127");
                if (m3424i == null) {
                    CompressHelper.this.m4975O0();
                    observableEmitter.onComplete();
                } else {
                    observableEmitter.onNext(m3424i);
                }
                observableEmitter.onComplete();
            }
        }).m7300i6(Schedulers.m5370e()).m7193t4(AndroidSchedulers.m8490e());
    }

    /* renamed from: o1 */
    public String m4889o1() {
        VBHelper vBHelper = this.f73788f;
        String m3410w = vBHelper.m3410w(vBHelper.m3421l());
        return m3410w == null ? "Nousername" : TextUtils.split(m3410w.replace("||", "::"), "::")[9];
    }

    /* renamed from: p */
    public void m4888p(Bundle bundle, String[] strArr, String str, int i) {
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(strArr).subList(i, strArr.length));
            String[] strArr2 = new String[arrayList.size()];
            arrayList.toArray(strArr2);
            this.f73786d.getContentResolver().update(Uri.parse("content://net.imedicaldoctor.imd/query"), null, m4945Y0(bundle, str), strArr2);
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("ExecuteDB" + bundle.getString("Name"), "Error in Executing DB , " + m4945Y0(bundle, str) + ", " + e.getLocalizedMessage());
        }
    }

    /* renamed from: p0 */
    public Observable<String> m4887p0(final String str) {
        return Observable.m7156x1(new ObservableOnSubscribe<String>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.9
            @Override // io.reactivex.rxjava3.core.ObservableOnSubscribe
            /* renamed from: a */
            public void mo3518a(@NonNull ObservableEmitter<String> observableEmitter) throws Throwable {
                try {
                    Thread.sleep(4000L);
                } catch (Exception e) {
                    FirebaseCrashlytics.m18030d().m18027g(e);
                }
                String m3424i = CompressHelper.this.f73788f.m3424i(CompressHelper.this.m4884q0(str), "127");
                if (m3424i == null) {
                    CompressHelper.this.m4975O0();
                    observableEmitter.onComplete();
                } else {
                    observableEmitter.onNext(m3424i);
                }
                observableEmitter.onComplete();
            }
        }).m7300i6(Schedulers.m5370e()).m7193t4(AndroidSchedulers.m8490e());
    }

    /* renamed from: p1 */
    public void m4886p1(Bundle bundle) {
        Class<?> cls;
        Class<?> cls2;
        String string = bundle.getString("Type");
        String string2 = bundle.getString("Name");
        Bundle bundle2 = new Bundle();
        bundle2.putBundle("DB", bundle);
        if (!this.f73788f.m3409x(bundle.getString("Name"), bundle.getString("Version"), new StringBuilder()) && !bundle.containsKey("Demu")) {
            Toast.makeText(this.f73786d, "You don't own this database, please Buy before use", 1).show();
            return;
        }
        m4869v0(bundle.getString("Name"), bundle.getString("Title"), m5012C(bundle));
        if (string.equals("elsevier") || string.equals("elseviernew")) {
            cls = ELSChaptersActivity.class;
            cls2 = ELSChaptersActivity.ELSChaptersFragment.class;
        } else if (string.equals("uptodate")) {
            cls = UTDSearchActivity.class;
            cls2 = UTDSearchActivity.UTDSearchFragment.class;
        } else if (string.equals("nejm")) {
            cls = NEJMTOCActivity.class;
            cls2 = NEJMTOCActivity.NEJMTOCFragment.class;
        } else if (string.equals("utdadvanced")) {
            cls = UTDASearchActivity.class;
            cls2 = UTDASearchActivityFragment.class;
        } else if (string.equals("accessmedicine")) {
            bundle2.putString("ParentId", "0");
            cls = AMChaptersActivity.class;
            cls2 = AMChaptersActivity.AMChaptersFragment.class;
        } else if (string2.equals("interact.db")) {
            cls = LXInteractList.class;
            cls2 = LXInteractList.LXInteractListFragment.class;
        } else if (string2.equals("ivcompat.db")) {
            cls = LXIvInteract.class;
            cls2 = LXIvInteract.LXIvInteractFragment.class;
        } else if (string.equals("skyscape")) {
            cls = SSSearchActivity.class;
            cls2 = SSSearchActivity.SSSearchFragment.class;
        } else if (string.equals("ovid")) {
            cls = OvidChaptersActivity.class;
            cls2 = OvidChaptersActivity.OvidChaptersFragment.class;
        } else if (string.equals("irandarou")) {
            cls = IDSearchActivity.class;
            cls2 = IDSearchActivity.IDSearchFragment.class;
        } else if (string.equals("uptodateddx")) {
            cls = UTDDSearchActivity.class;
            cls2 = UTDDSearchActivity.UTDDSearchFragment.class;
        } else if (string.equals("visualdx")) {
            cls = VDSearchActivity.class;
            cls2 = VDSearchActivity.VDSearchFragment.class;
        } else if (string.equals("visualdxddx")) {
            cls = VDDxScenarioActivity.class;
            cls2 = VDDxScenarioActivity.VDDxScenarioFragment.class;
        } else if (string.equals("Dictionary")) {
            cls = CDicSearchActivity.class;
            cls2 = CDicSearchActivity.CDicSearchFragment.class;
        } else if (string.equals("medhand")) {
            cls = MHSearchActivity.class;
            cls2 = MHSearchActivity.MHSearchFragment.class;
        } else if (string.equals("epub")) {
            bundle2.putString("ParentId", "0");
            cls = EPUBChaptersActivity.class;
            cls2 = EPUBChaptersActivityFragment.class;
        } else if (string.equals("epocrate")) {
            cls = EPOMainActivity.class;
            cls2 = EPOMainActivityFragment.class;
        } else if (string.equals("martindale")) {
            cls = MDListActivity.class;
            cls2 = MDListActivityFragment.class;
        } else if (string.equals("amirsys")) {
            cls = ASListActivity.class;
            cls2 = ASListActivityFragment.class;
        } else if (string.equals("statdx")) {
            cls = SDListActivity.class;
            cls2 = SDListActivityFragment.class;
        } else if (string.equals("facts")) {
            cls = FTListActivity.class;
            cls2 = FTListActivityFragment.class;
        } else if (string.equals("micromedex-drug")) {
            cls = MMListActivity.class;
            cls2 = MMListActivityFragment.class;
        } else if (string.equals("micromedex-neofax")) {
            cls = MMNeoListActivity.class;
            cls2 = MMNeoListActivityFragment.class;
        } else if (string.equals("micromedex-interact")) {
            cls = MMInteractSelectActivity.class;
            cls2 = MMInteractSelectActivityFragment.class;
        } else if (string.equals("micromedex-iv")) {
            cls = MMIVSelectActivity.class;
            cls2 = MMIVSelectActivityFragment.class;
        } else if (string.equals("sanford")) {
            cls = SANTocActivity.class;
            cls2 = SANTocActivityFragment.class;
        } else if (string.equals("uworld")) {
            cls = UWMainActivity.class;
            cls2 = UWMainActivityFragment.class;
        } else if (string.equals("irqbank")) {
            cls = DREMainActivity.class;
            cls2 = DREMainActivityFragment.class;
        } else if (string.equals("noskhe")) {
            cls = NOSListActivity.class;
            cls2 = NOSListActivityFragment.class;
        } else if (string.equals("irandrugs")) {
            cls = IranGenericDrugsList.class;
            cls2 = IranGenericDrugsListFragment.class;
        } else if (string.equals("mksap")) {
            m4883q1(bundle, "", null, null);
            return;
        } else if (string.equals("stockley")) {
            cls = STListActivity.class;
            cls2 = STListActivityFragment.class;
        } else if (string.equals("lww")) {
            bundle2.putString("ParentId", "0");
            cls = LWWChapters.class;
            cls2 = LWWChaptersFragment.class;
        } else if (string.equals("cme") || string.equals("kaptest")) {
            bundle2.putString("ParentId", "0");
            m4979N(CMETOC.class, CMETOCFragment.class, bundle2);
            return;
        } else if (string.equals("tol")) {
            cls = PsychoListActivity.class;
            cls2 = PsychoListActivityFragment.class;
        } else {
            cls = LXItems.class;
            cls2 = LXItems.LXItemsFragment.class;
        }
        m4979N(cls, cls2, bundle2);
    }

    /* renamed from: q */
    public void m4885q(String str, String str2) {
        try {
            this.f73786d.getContentResolver().update(Uri.parse("content://net.imedicaldoctor.imd/query"), null, str, new String[]{str2});
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("ExecuteDB", "Error in Executing DB , " + str + ", " + e.getLocalizedMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x010f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* renamed from: q0 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String m4884q0(java.lang.String r9) {
        /*
            Method dump skipped, instructions count: 299
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.imedicaldoctor.imd.Data.CompressHelper.m4884q0(java.lang.String):java.lang.String");
    }

    /* renamed from: q1 */
    public void m4883q1(Bundle bundle, String str, String[] strArr, String str2) {
        m4880r1(bundle, str, strArr, str2, null);
    }

    /* renamed from: r */
    public void m4882r(String str, String[] strArr, int i) {
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(strArr).subList(i, strArr.length));
            String[] strArr2 = new String[arrayList.size()];
            arrayList.toArray(strArr2);
            this.f73786d.getContentResolver().update(Uri.parse("content://net.imedicaldoctor.imd/query"), null, str, strArr2);
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("ExecuteDB" + str, "Error in Executing DB , " + str + ", " + e.getLocalizedMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x010f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* renamed from: r0 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String m4881r0(byte[] r9) {
        /*
            Method dump skipped, instructions count: 299
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.imedicaldoctor.imd.Data.CompressHelper.m4881r0(byte[]):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:221:0x05d1  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x05da  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x05ea  */
    /* JADX WARN: Removed duplicated region for block: B:452:0x0afd  */
    /* JADX WARN: Removed duplicated region for block: B:455:0x0b06  */
    /* JADX WARN: Removed duplicated region for block: B:458:0x0b16  */
    /* renamed from: r1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void m4880r1(android.os.Bundle r28, java.lang.String r29, java.lang.String[] r30, java.lang.String r31, android.os.Bundle r32) {
        /*
            Method dump skipped, instructions count: 2890
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: net.imedicaldoctor.imd.Data.CompressHelper.m4880r1(android.os.Bundle, java.lang.String, java.lang.String[], java.lang.String, android.os.Bundle):void");
    }

    /* renamed from: s */
    public void m4879s(String str) {
        try {
            this.f73786d.getContentResolver().update(Uri.parse("content://net.imedicaldoctor.imd/query"), null, m4972P0(), new String[]{str});
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            iMDLogger.m3294f("ExecuteDB", "Error in Executing DB , " + m4972P0() + ", " + e.getLocalizedMessage());
        }
    }

    /* renamed from: s0 */
    public String m4878s0(String str, String str2, String str3, Bundle bundle) {
        ArrayList<Bundle> m4946Y = m4946Y(str, str2);
        String[] splitByWholeSeparatorPreserveAllTokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(str3, ",");
        if (m4946Y == null) {
            return "";
        }
        ArrayList arrayList = new ArrayList();
        Iterator<Bundle> it2 = m4946Y.iterator();
        while (it2.hasNext()) {
            Bundle next = it2.next();
            ArrayList arrayList2 = new ArrayList();
            for (String str4 : splitByWholeSeparatorPreserveAllTokens) {
                String string = next.getString(str4);
                if (bundle != null && bundle.containsKey(str4)) {
                    string = bundle.getString(str4);
                }
                arrayList2.add(string);
            }
            arrayList.add(TextUtils.join(":::", arrayList2));
        }
        return TextUtils.join(";;;", arrayList);
    }

    /* renamed from: s1 */
    public String m4877s1(File file) {
        String l = Long.toString(file.length());
        byte[] bArr = new byte[1048576];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            IOUtils.read(fileInputStream, bArr, 0, 1048576);
            fileInputStream.close();
            CRC32 crc32 = new CRC32();
            crc32.update(bArr, 0, 1048576);
            String l2 = Long.toString(crc32.getValue());
            return l2 + l;
        } catch (Exception unused) {
            return null;
        }
    }

    /* renamed from: t */
    public int m4876t(String str) {
        String[] splitByWholeSeparatorPreserveAllTokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(str, ";;;");
        if (splitByWholeSeparatorPreserveAllTokens.length > 0) {
            return StringUtils.splitByWholeSeparatorPreserveAllTokens(splitByWholeSeparatorPreserveAllTokens[0], ":::").length;
        }
        return 0;
    }

    /* renamed from: t0 */
    public void m4875t0(String str, String str2, String str3) {
        String m4963S0 = m4963S0(str);
        String m4963S02 = m4963S0(str2);
        String m4963S03 = m4963S0(str3);
        String m4863x0 = m4863x0();
        m4885q(m4863x0, "Insert into cache (cachekey, cachecontent, cachevalidation) values ('" + m4963S0 + "', '" + m4963S02 + "', '" + m4963S03 + "')");
    }

    /* renamed from: u */
    public String m4873u(String str) {
        String[] splitByWholeSeparator = StringUtils.splitByWholeSeparator(str, "/");
        return splitByWholeSeparator[0] + "/" + splitByWholeSeparator[1];
    }

    /* renamed from: u0 */
    public void m4872u0(String str, String str2, String str3, String str4) {
        String m4963S0 = m4963S0(str);
        String m4963S02 = m4963S0(str2);
        String m4963S03 = m4963S0(str4);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String m4971P1 = m4971P1();
        m4885q(m4971P1, "Insert into recent (dbName, dbTitle, dbAddress, dbDate, dbDocName) values ('" + m4963S0 + "', '" + m4963S02 + "', '" + str3 + "', '" + format + "', '" + m4963S03 + "')");
    }

    /* renamed from: u1 */
    public void m4871u1() {
        Bundle m4907i1 = m4907i1(m4946Y(m4971P1(), "Select c from r where id=1"));
        if (m4907i1 == null) {
            m4885q(m4971P1(), "insert into r values (1,0)");
            return;
        }
        String m4971P1 = m4971P1();
        m4885q(m4971P1, "update r set c=" + (Integer.valueOf(m4907i1.getString("c")).intValue() + 1) + " where id=1");
    }

    /* renamed from: v */
    public byte[] m4870v(String str, String str2, String str3) {
        if (str3.equals("127")) {
            try {
                VBHelper vBHelper = this.f73788f;
                String str4 = TextUtils.split(vBHelper.m3410w(vBHelper.m3421l()).replace("||", "::"), "::")[1];
                byte[] decode = Base64.decode(str, 0);
                for (int length = str2.length(); length < 8; length++) {
                    str2 = str2 + StringUtils.SPACE;
                }
                try {
                    try {
                        return m4951W0(m5002F0(str4.toCharArray(), str2.getBytes("UTF-8"), new byte[]{17, 115, 105, 102, 103, 104, 111, 107, 108, 122, 120, 119, 118, 98, 110, 109}, decode));
                    } catch (Exception e) {
                        iMDLogger.m3294f("CompressHelper _ GetData Decompressing", e.toString());
                        return null;
                    }
                } catch (Exception e2) {
                    iMDLogger.m3294f("CompressHelper _ GetData Decryption", e2.toString());
                    return null;
                }
            } catch (Exception e3) {
                FirebaseCrashlytics.m18030d().m18027g(e3);
                FirebaseCrashlytics.m18030d().m18027g(e3);
                return null;
            }
        }
        return null;
    }

    /* renamed from: v0 */
    public void m4869v0(String str, String str2, String str3) {
        String m4963S0 = m4963S0(str);
        String m4963S02 = m4963S0(str2);
        String m4963S03 = m4963S0(str3);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String m4971P1 = m4971P1();
        m4885q(m4971P1, "Insert into dbrecent (dbName, dbTitle, dbDate, dbIcon) values ('" + m4963S0 + "', '" + m4963S02 + "', '" + format + "', '" + m4963S03 + "')");
    }

    /* renamed from: w */
    public byte[] m4867w(byte[] bArr, String str, String str2) {
        if (str2.equals("127")) {
            VBHelper vBHelper = this.f73788f;
            String str3 = TextUtils.split(vBHelper.m3410w(vBHelper.m3421l()).replace("||", "::"), "::")[1];
            for (int length = str.length(); length < 8; length++) {
                str = str + StringUtils.SPACE;
            }
            try {
                return m5002F0(str3.toCharArray(), str.getBytes("UTF-8"), new byte[]{17, 115, 105, 102, 103, 104, 111, 107, 108, 122, 120, 119, 118, 98, 110, 109}, bArr);
            } catch (Exception e) {
                iMDLogger.m3294f("CompressHelper _ GetData Decryption", e.toString());
                return null;
            }
        }
        return null;
    }

    /* renamed from: w0 */
    public String m4866w0(String str, String str2) {
        String m4963S0 = m4963S0(str);
        String m4863x0 = m4863x0();
        Bundle m4926d1 = m4926d1(m4946Y(m4863x0, "Select * from cache where cachekey='" + m4963S0 + "'"));
        if (m4926d1 == null) {
            return null;
        }
        if (m4926d1.getString("cachevalidation").equals(str2)) {
            return m4926d1.getString("cachecontent");
        }
        String m4863x02 = m4863x0();
        m4885q(m4863x02, "Delete form cache where cachekey = '" + m4963S0 + "'");
        return null;
    }

    /* renamed from: x */
    public byte[] m4864x(byte[] bArr, String str, String str2) {
        if (str2.equals("127")) {
            VBHelper vBHelper = this.f73788f;
            String str3 = TextUtils.split(vBHelper.m3410w(vBHelper.m3421l()).replace("||", "::"), "::")[1];
            for (int length = str.length(); length < 8; length++) {
                str = str + StringUtils.SPACE;
            }
            try {
                return m5002F0(str3.toCharArray(), str.getBytes("UTF-8"), new byte[]{17, 115, 105, 102, 103, 104, 111, 107, 108, 122, 120, 119, 118, 98, 110, 109}, bArr);
            } catch (Exception e) {
                iMDLogger.m3294f("CompressHelper _ GetData Decryption", e.toString());
                return null;
            }
        }
        return null;
    }

    /* renamed from: x0 */
    public String m4863x0() {
        String str = m4856z1() + "/cache.db";
        if (!new File(str).exists()) {
            SQLiteDatabase.openOrCreateDatabase(str, (SQLiteDatabase.CursorFactory) null).execSQL("create table cache (id integer primary key autoincrement, cachekey varchar(255) unique, cachecontent text, cachevalidation text);");
        }
        return str;
    }

    /* renamed from: y */
    public String m4861y() {
        String m4856z1;
        if (((iMD) this.f73786d.getApplicationContext()).f83459Y != null) {
            return ((iMD) this.f73786d.getApplicationContext()).f83459Y;
        }
        try {
            m4856z1 = PreferenceManager.getDefaultSharedPreferences(this.f73786d).getString("DownloadPath", "");
            if (m4856z1.length() == 0 || !new File(m4856z1).exists()) {
                m4856z1 = m4856z1();
            }
            if (!m4922e1().contains(m4856z1)) {
                m4856z1 = m4856z1();
                PreferenceManager.getDefaultSharedPreferences(this.f73786d).edit().remove("DownloadPath").commit();
                PreferenceManager.getDefaultSharedPreferences(this.f73786d).edit().putString("DownloadPath", m4856z1).commit();
            }
        } catch (Exception e) {
            FirebaseCrashlytics.m18030d().m18027g(e);
            FirebaseCrashlytics.m18030d().m18027g(e);
            m4856z1 = m4856z1();
        }
        ((iMD) this.f73786d.getApplicationContext()).f83459Y = m4856z1;
        return m4856z1;
    }

    /* renamed from: y0 */
    public String m4860y0(String str) {
        if (str.contains(") order by RANDOM() limit 3")) {
            return str;
        }
        return "Select * from (" + str + ") order by RANDOM() limit 3";
    }

    /* renamed from: y1 */
    public ArrayList<Bundle> m4859y1() {
        String str = m4856z1() + "/databases.json";
        if (((iMD) this.f73786d.getApplicationContext()).f83461s == null) {
            m4902k0();
            if (((iMD) this.f73786d.getApplicationContext()).f83461s == null) {
                ((iMD) this.f73786d.getApplicationContext()).f83461s = new ArrayList<>();
            }
        }
        ArrayList<Bundle> arrayList = new ArrayList<>();
        Iterator<Bundle> it2 = ((iMD) this.f73786d.getApplicationContext()).f83461s.iterator();
        while (it2.hasNext()) {
            arrayList.add(it2.next());
        }
        Collections.sort(arrayList, new Comparator<Bundle>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.15
            @Override // java.util.Comparator
            /* renamed from: a */
            public int compare(Bundle bundle, Bundle bundle2) {
                return bundle.getString("Section").compareTo(bundle.getString("Section"));
            }
        });
        if (new File(str).exists()) {
            try {
                JSONArray jSONArray = new JSONArray(new String(m4980M1(str)));
                ArrayList<Bundle> arrayList2 = new ArrayList<>();
                int i = 0;
                while (i < jSONArray.length()) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    JSONArray jSONArray2 = jSONObject.getJSONArray(FirebaseAnalytics.Param.f55203f0);
                    ArrayList<? extends Parcelable> arrayList3 = new ArrayList<>();
                    int i2 = 0;
                    while (i2 < jSONArray2.length()) {
                        final JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                        ArrayList arrayList4 = new ArrayList(Collections2.m23110e(arrayList, new Predicate<Bundle>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.16
                            @Override // com.google.common.base.Predicate
                            /* renamed from: a */
                            public boolean apply(Bundle bundle) {
                                try {
                                    return bundle.getString("Name").equals(jSONObject2.getString("Name"));
                                } catch (Exception e) {
                                    FirebaseCrashlytics.m18030d().m18027g(e);
                                    iMDLogger.m3294f("Error in filtering", e.getLocalizedMessage());
                                    return false;
                                }
                            }
                        }));
                        JSONArray jSONArray3 = jSONArray;
                        if (arrayList4.size() == 1) {
                            Bundle bundle = (Bundle) arrayList4.get(0);
                            arrayList.remove(bundle);
                            if (jSONObject2.has("dontSearch") && jSONObject2.getString("dontSearch").equals(IcyHeaders.f35463C2)) {
                                bundle.putString("dontSearch", IcyHeaders.f35463C2);
                            } else {
                                bundle.putString("dontSearch", "0");
                            }
                            arrayList3.add(bundle);
                        }
                        i2++;
                        jSONArray = jSONArray3;
                    }
                    JSONArray jSONArray4 = jSONArray;
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("title", jSONObject.getString("title"));
                    bundle2.putParcelableArrayList(FirebaseAnalytics.Param.f55203f0, arrayList3);
                    arrayList2.add(bundle2);
                    i++;
                    jSONArray = jSONArray4;
                }
                if (arrayList.size() > 0) {
                    ArrayList<Bundle> m4944Y1 = m4944Y1(arrayList);
                    for (int i3 = 0; i3 < m4944Y1.size(); i3++) {
                        final Bundle bundle3 = m4944Y1.get(i3);
                        ArrayList arrayList5 = new ArrayList(Collections2.m23110e(arrayList2, new Predicate<Bundle>() { // from class: net.imedicaldoctor.imd.Data.CompressHelper.17
                            @Override // com.google.common.base.Predicate
                            /* renamed from: a */
                            public boolean apply(Bundle bundle4) {
                                return bundle4.getString("title").equals(bundle3.getString("title"));
                            }
                        }));
                        if (arrayList5.size() == 0) {
                            Bundle bundle4 = new Bundle();
                            bundle4.putString("title", bundle3.getString("title"));
                            bundle4.putParcelableArrayList(FirebaseAnalytics.Param.f55203f0, bundle3.getParcelableArrayList(FirebaseAnalytics.Param.f55203f0));
                            arrayList2.add(bundle4);
                        } else {
                            Bundle bundle5 = (Bundle) arrayList5.get(0);
                            ArrayList<? extends Parcelable> parcelableArrayList = bundle5.getParcelableArrayList(FirebaseAnalytics.Param.f55203f0);
                            parcelableArrayList.addAll(bundle3.getParcelableArrayList(FirebaseAnalytics.Param.f55203f0));
                            int indexOf = arrayList2.indexOf(bundle5);
                            Bundle bundle6 = new Bundle();
                            bundle6.putString("title", bundle5.getString("title"));
                            bundle6.putParcelableArrayList(FirebaseAnalytics.Param.f55203f0, parcelableArrayList);
                            arrayList2.remove(indexOf);
                            arrayList2.add(indexOf, bundle6);
                        }
                    }
                }
                return arrayList2;
            } catch (Exception e) {
                FirebaseCrashlytics.m18030d().m18027g(e);
                iMDLogger.m3294f("Error in Reading Json", e.getLocalizedMessage());
                return m4944Y1(arrayList);
            }
        }
        return m4944Y1(arrayList);
    }

    /* renamed from: z */
    public Bundle m4858z(ArrayList<Bundle> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        return arrayList.get(arrayList.size() - 1);
    }

    /* renamed from: z1 */
    public String m4856z1() {
        return m5004E1();
    }
}
